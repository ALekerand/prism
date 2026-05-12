import argparse
import csv
import hashlib
import io
import json
import re
from pathlib import Path


HEADER = "DRENA\tIEPP\tRégion\tDépartement\tCommune\tLocalité\tNom du Centre"


def norm(value: str | None) -> str:
    return re.sub(r"\s+", " ", (value or "").strip())


def trunc(value: str, max_len: int) -> str:
    return norm(value)[:max_len]


def sql(value: str | int | None) -> str:
    if value is None:
        return "NULL"
    if isinstance(value, int):
        return str(value)
    return "'" + value.replace("\\", "\\\\").replace("'", "''") + "'"


def sql_bit(value: bool | None) -> str:
    if value is None:
        return "NULL"
    return "b'1'" if value else "b'0'"


def code(prefix: str, value: str, max_len: int = 10) -> str:
    clean = re.sub(r"[^A-Z0-9]", "", norm(value).upper())
    digest = hashlib.sha1(norm(value).encode("utf-8")).hexdigest().upper()[:6]
    base = clean[: max(0, max_len - len(prefix) - 1 - len(digest))]
    return f"{prefix}-{base}{digest}"[:max_len]


def centre_code(row: dict[str, str], index: int) -> str:
    key = "|".join(
        norm(row.get(k))
        for k in ["DRENA", "IEPP", "Région", "Département", "Commune", "Localité", "Nom du Centre"]
    )
    digest = hashlib.sha1(key.encode("utf-8")).hexdigest().upper()[:10]
    return f"IMP-{digest}"


def centre_type(value: str) -> str | None:
    upper = norm(value).upper()
    if "CEC" in upper:
        return "CEC"
    if "SIE" in upper:
        return "SIE"
    if "CP" in upper or "PASSERELLE" in upper:
        return "CP"
    if "ALPHA" in upper or "ALPHAB" in upper:
        return "ALPHA"
    return None


def int_or_null(value: str) -> int | None:
    v = norm(value)
    if not v:
        return None
    try:
        return int(float(v.replace(",", ".")))
    except ValueError:
        return None


def bool_or_null(value: str) -> bool | None:
    v = norm(value).upper()
    if v in {"VRAI", "TRUE", "1", "OUI", "YES"}:
        return True
    if v in {"FAUX", "FALSE", "0", "NON", "NO"}:
        return False
    return None


def split_commune(raw: str, departement: str) -> tuple[str, str]:
    value = norm(raw)
    if "/" in value:
        first, second = [norm(x) for x in value.split("/", 1)]
        return first or departement, second or first or departement
    return value or departement, value or departement


def load_rows(transcript: Path) -> list[dict[str, str]]:
    text = ""
    with transcript.open(encoding="utf-8") as fh:
        for line in fh:
            try:
                event = json.loads(line)
            except json.JSONDecodeError:
                continue
            if event.get("role") != "user":
                continue
            for content in event.get("message", {}).get("content", []):
                item = content.get("text", "") if isinstance(content, dict) else ""
                if HEADER in item:
                    text = item
                    break
            if text:
                break
    if not text:
        raise SystemExit("Aucune liste de centres trouvee dans le transcript.")
    body = text[text.find(HEADER) :].split("</user_query>")[0].strip()
    return list(csv.DictReader(io.StringIO(body), delimiter="\t"))


def row_is_usable(row: dict[str, str]) -> bool:
    required = ["DRENA", "IEPP", "Région", "Département", "Commune", "Localité", "Nom du Centre", "Promoteur"]
    return all(norm(row.get(k)) for k in required) and centre_type(row.get("Type d'Intervention", "")) is not None


def generate_sql(rows: list[dict[str, str]]) -> tuple[str, dict[str, int]]:
    usable = []
    seen_codes = set()
    skipped = 0
    type_counts: dict[str, int] = {}

    for i, row in enumerate(rows, start=1):
        if not row_is_usable(row):
            skipped += 1
            continue
        ctype = centre_type(row.get("Type d'Intervention", ""))
        ccode = centre_code(row, i)
        if ccode in seen_codes:
            skipped += 1
            continue
        seen_codes.add(ccode)
        type_counts[ctype] = type_counts.get(ctype, 0) + 1
        usable.append((i, ctype, ccode, row))

    out = [
        "SET FOREIGN_KEY_CHECKS = 1;",
        "START TRANSACTION;",
        "",
        "-- Valeurs de reference minimales necessaires a l'import.",
        "INSERT INTO milieu_implantation (code_milieu_implentation, libelle_type_implentation_)",
        "SELECT 'ND', 'ND' WHERE NOT EXISTS (SELECT 1 FROM milieu_implantation WHERE code_milieu_implentation = 'ND');",
        "SET @id_milieu := (SELECT id_milieu_implentation FROM milieu_implantation WHERE code_milieu_implentation = 'ND' LIMIT 1);",
        "SET @id_nature := (SELECT id_naturecentre FROM naturecentre ORDER BY id_naturecentre LIMIT 1);",
        "SET @id_campagne := (SELECT id_compagne FROM campagne ORDER BY id_compagne LIMIT 1);",
        "SET @id_categorie_alpha := (SELECT id_categorie_centre_alpha FROM categorie_centre_alpha ORDER BY id_categorie_centre_alpha LIMIT 1);",
        "SET @id_regime_alpha := (SELECT id_regime_alpha FROM regimealphabetisation ORDER BY id_regime_alpha LIMIT 1);",
        "",
    ]

    for index, ctype, ccode, row in usable:
        region = trunc(row["Région"], 100)
        drena = trunc(row["DRENA"], 25)
        iep = trunc(row["IEPP"], 25)
        dep = trunc(row["Département"], 25)
        commune, sous_pref = split_commune(row["Commune"], dep)
        commune = trunc(commune, 25)
        sous_pref = trunc(sous_pref, 25)
        localite = trunc(row["Localité"], 25)
        centre = trunc(row["Nom du Centre"], 100)
        promoteur = trunc(row["Promoteur"], 100)
        total = int_or_null(row.get("Total Apprenants", ""))
        hommes = int_or_null(row.get("Hommes/Garçons", ""))
        femmes = int_or_null(row.get("Femmes/Filles", ""))
        latitude = trunc(row.get("Latitude", ""), 50)
        longitude = trunc(row.get("Longitude", ""), 50)
        gps = bool_or_null(row.get("GPS Valide", ""))
        structure_partenaire = trunc(row.get("Structure Partenaire", ""), 150)
        nom_partenaire = trunc(row.get("Nom du Partenaire", ""), 150)
        type_alpha = trunc(row.get("Type Alphabétisation", ""), 50) or "Non renseigné"
        niveau_alpha = trunc(row.get("Niveau Alphabétisation", ""), 100)

        out.extend(
            [
                f"-- Ligne source {index}: {centre}",
                f"INSERT INTO region (code_region, libelle_region) SELECT {sql(code('R', region))}, {sql(region)} WHERE NOT EXISTS (SELECT 1 FROM region WHERE libelle_region = {sql(region)});",
                f"SET @id_region := (SELECT id_region FROM region WHERE libelle_region = {sql(region)} ORDER BY id_region LIMIT 1);",
                f"INSERT INTO drena (code_drena, nom_drena) SELECT {sql(code('D', drena))}, {sql(drena)} WHERE NOT EXISTS (SELECT 1 FROM drena WHERE nom_drena = {sql(drena)});",
                f"SET @id_drena := (SELECT id_drena FROM drena WHERE nom_drena = {sql(drena)} ORDER BY id_drena LIMIT 1);",
                f"INSERT INTO departement (code_departement, nom_departement, id_region) SELECT {sql(code('DP', dep))}, {sql(dep)}, @id_region WHERE NOT EXISTS (SELECT 1 FROM departement WHERE nom_departement = {sql(dep)});",
                f"SET @id_departement := (SELECT id_departement FROM departement WHERE nom_departement = {sql(dep)} ORDER BY id_departement LIMIT 1);",
                "INSERT INTO drena_departement (id_drena, id_departement) SELECT @id_drena, @id_departement WHERE NOT EXISTS (SELECT 1 FROM drena_departement WHERE id_drena = @id_drena AND id_departement = @id_departement);",
                f"INSERT INTO iep (id_drena, code_iep, nom_iep) SELECT @id_drena, {sql(code('I', iep))}, {sql(iep)} WHERE NOT EXISTS (SELECT 1 FROM iep WHERE id_drena = @id_drena AND nom_iep = {sql(iep)});",
                f"SET @id_iep := (SELECT id_iep FROM iep WHERE id_drena = @id_drena AND nom_iep = {sql(iep)} ORDER BY id_iep LIMIT 1);",
                f"INSERT INTO sous_prefecture (id_departement, code_sous_prefecture, nom_sous_prefecture) SELECT @id_departement, {sql(code('SP', sous_pref))}, {sql(sous_pref)} WHERE NOT EXISTS (SELECT 1 FROM sous_prefecture WHERE id_departement = @id_departement AND nom_sous_prefecture = {sql(sous_pref)});",
                f"SET @id_sous_prefecture := (SELECT id_sous_prefecture FROM sous_prefecture WHERE id_departement = @id_departement AND nom_sous_prefecture = {sql(sous_pref)} ORDER BY id_sous_prefecture LIMIT 1);",
                f"INSERT INTO commune (code_commune, nom_commune) SELECT {sql(code('C', commune))}, {sql(commune)} WHERE NOT EXISTS (SELECT 1 FROM commune WHERE nom_commune = {sql(commune)});",
                f"SET @id_commune := (SELECT id_commune FROM commune WHERE nom_commune = {sql(commune)} ORDER BY id_commune LIMIT 1);",
                f"INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, code_localite, nom_localite) SELECT @id_commune, @id_milieu, @id_sous_prefecture, {sql(code('L', localite))}, {sql(localite)} WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sous_prefecture AND nom_localite = {sql(localite)});",
                f"SET @id_localite := (SELECT id_localite FROM localite_d_implantation WHERE id_sous_prefecture = @id_sous_prefecture AND nom_localite = {sql(localite)} ORDER BY id_localite LIMIT 1);",
                f"INSERT INTO promoteur (code_promoteur, libelle_promoteur, type_promoteur) SELECT {sql(code('P', promoteur, 50))}, {sql(promoteur)}, 'PHYSIQUE' WHERE NOT EXISTS (SELECT 1 FROM promoteur WHERE libelle_promoteur = {sql(promoteur)});",
                f"SET @id_promoteur := (SELECT id_promoteur FROM promoteur WHERE libelle_promoteur = {sql(promoteur)} ORDER BY id_promoteur LIMIT 1);",
                f"INSERT INTO centre (id_iep, id_localite, id_naturecentre, id_promoteur, code_centre, localisation_centre, nom_milieu_implentation, total_apprenants, total_hommes, total_femmes, latitude_gps, longitude_gps, gps_valide, structure_partenaire, nom_partenaire) SELECT @id_iep, @id_localite, @id_nature, @id_promoteur, {sql(ccode)}, {sql(centre)}, 'ND', {sql(total)}, {sql(hommes)}, {sql(femmes)}, {sql(latitude) if latitude else 'NULL'}, {sql(longitude) if longitude else 'NULL'}, {sql_bit(gps)}, {sql(structure_partenaire) if structure_partenaire else 'NULL'}, {sql(nom_partenaire) if nom_partenaire else 'NULL'} WHERE NOT EXISTS (SELECT 1 FROM centre WHERE code_centre = {sql(ccode)});",
                f"SET @id_centre := (SELECT id_centre FROM centre WHERE code_centre = {sql(ccode)} ORDER BY id_centre LIMIT 1);",
                f"UPDATE centre SET total_apprenants = {sql(total)}, total_hommes = {sql(hommes)}, total_femmes = {sql(femmes)}, latitude_gps = {sql(latitude) if latitude else 'NULL'}, longitude_gps = {sql(longitude) if longitude else 'NULL'}, gps_valide = {sql_bit(gps)}, structure_partenaire = {sql(structure_partenaire) if structure_partenaire else 'NULL'}, nom_partenaire = {sql(nom_partenaire) if nom_partenaire else 'NULL'} WHERE id_centre = @id_centre;",
            ]
        )

        common = (
            "a_de_leau, autorisation, encadrer_par_mena, est_electrifie, id_centre, id_iep, id_localite, "
            "id_naturecentre, id_promoteur, code_centre, localisation_centre, nom_milieu_implentation, "
            "total_apprenants, total_hommes, total_femmes, latitude_gps, longitude_gps, gps_valide, "
            "structure_partenaire, nom_partenaire"
        )
        values = (
            f"NULL, NULL, NULL, NULL, @id_centre, @id_iep, @id_localite, @id_nature, @id_promoteur, {sql(ccode)}, "
            f"{sql(centre)}, 'ND', {sql(total)}, {sql(hommes)}, {sql(femmes)}, {sql(latitude) if latitude else 'NULL'}, "
            f"{sql(longitude) if longitude else 'NULL'}, {sql_bit(gps)}, {sql(structure_partenaire) if structure_partenaire else 'NULL'}, "
            f"{sql(nom_partenaire) if nom_partenaire else 'NULL'}"
        )

        if ctype == "ALPHA":
            out.extend(
                [
                    f"INSERT INTO type_alpha (libelle_type_alpha) SELECT {sql(type_alpha)} WHERE NOT EXISTS (SELECT 1 FROM type_alpha WHERE libelle_type_alpha = {sql(type_alpha)});",
                    f"SET @id_type_alpha := (SELECT id_type_alpha FROM type_alpha WHERE libelle_type_alpha = {sql(type_alpha)} ORDER BY id_type_alpha LIMIT 1);",
                    f"INSERT INTO alpha ({common}, id_categorie_centre_alpha, id_compagne, id_regime_alpha, id_type_alpha, code_alpha, libelle_alpha) SELECT {values}, @id_categorie_alpha, @id_campagne, @id_regime_alpha, @id_type_alpha, {sql(ccode)}, {sql(centre)} WHERE NOT EXISTS (SELECT 1 FROM alpha WHERE id_centre = @id_centre);",
                ]
            )
            if niveau_alpha:
                out.append(
                    f"INSERT INTO niveau_alpha (id_centre, code_niveau_alpha, libelle_niveau_alpha) SELECT @id_centre, {sql(code('NA', niveau_alpha, 50))}, {sql(niveau_alpha)} WHERE NOT EXISTS (SELECT 1 FROM niveau_alpha WHERE id_centre = @id_centre AND libelle_niveau_alpha = {sql(niveau_alpha)});"
                )
        elif ctype == "CEC":
            out.append(
                f"INSERT INTO cec ({common}, libelle_cec) SELECT {values}, {sql(centre)} WHERE NOT EXISTS (SELECT 1 FROM cec WHERE id_centre = @id_centre);"
            )
        elif ctype == "CP":
            out.append(
                f"INSERT INTO cp ({common}, libellle_cp) SELECT {values}, {sql(centre)} WHERE NOT EXISTS (SELECT 1 FROM cp WHERE id_centre = @id_centre);"
            )
        elif ctype == "SIE":
            out.append(
                f"INSERT INTO sie ({common}, libelle_sie) SELECT {values}, {sql(centre)} WHERE NOT EXISTS (SELECT 1 FROM sie WHERE id_centre = @id_centre);"
            )
        out.append("")

    out.extend(["COMMIT;", ""])
    stats = {"input_rows": len(rows), "usable_rows": len(usable), "skipped_rows": skipped, **type_counts}
    return "\n".join(out), stats


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--transcript", required=True, type=Path)
    parser.add_argument("--out", required=True, type=Path)
    parser.add_argument("--stats", required=True, type=Path)
    args = parser.parse_args()

    rows = load_rows(args.transcript)
    sql_text, stats = generate_sql(rows)
    args.out.write_text(sql_text, encoding="utf-8")
    args.stats.write_text(json.dumps(stats, ensure_ascii=False, indent=2), encoding="utf-8")
    print(json.dumps(stats, ensure_ascii=False))


if __name__ == "__main__":
    main()
