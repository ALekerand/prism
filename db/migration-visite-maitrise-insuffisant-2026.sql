-- Visite : valeur stockée MAUVAISE → INSUFFISANT (maîtrise des séances).
UPDATE visite SET maitrise_seance_lecture = 'INSUFFISANT' WHERE UPPER(TRIM(maitrise_seance_lecture)) = 'MAUVAISE';
UPDATE visite SET maitrise_seance_ecriture = 'INSUFFISANT' WHERE UPPER(TRIM(maitrise_seance_ecriture)) = 'MAUVAISE';
UPDATE visite SET maitrise_seance_calcul = 'INSUFFISANT' WHERE UPPER(TRIM(maitrise_seance_calcul)) = 'MAUVAISE';
UPDATE visite SET maitrise_seance_cvc = 'INSUFFISANT' WHERE UPPER(TRIM(maitrise_seance_cvc)) = 'MAUVAISE';
