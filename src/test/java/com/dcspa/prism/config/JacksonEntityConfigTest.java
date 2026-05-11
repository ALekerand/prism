package com.dcspa.prism.config;

import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Designation;
import com.dcspa.prism.entity.RessourceFinanciereMateriel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JacksonEntityConfigTest {

	@Test
	void deserializeEntityWithScalarReferences() throws Exception {
		JacksonEntityConfig config = new JacksonEntityConfig();
		SimpleModule module = config.entityFormatBModule();
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(module);

		String json = """
				{
				  "idCentre": 1,
				  "idDesignation": 2,
				  "sourceFinancement": "sourceFinancement test",
				  "montant": 100
				}
				""";

		RessourceFinanciereMateriel value = mapper.readValue(json, RessourceFinanciereMateriel.class);

		assertNotNull(value);
		assertNotNull(value.getIdCentre());
		assertNotNull(value.getIdDesignation());
		assertEquals(1, value.getIdCentre().getId());
		assertEquals(2, value.getIdDesignation().getId());
	}

	@Test
	void deserializeEntityWithObjectReferences() throws Exception {
		JacksonEntityConfig config = new JacksonEntityConfig();
		SimpleModule module = config.entityFormatBModule();
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(module);

		String json = """
				{
				  "idCentre": { "id": 7 },
				  "idDesignation": { "id": 9 }
				}
				""";

		RessourceFinanciereMateriel value = mapper.readValue(json, RessourceFinanciereMateriel.class);

		assertNotNull(value);
		Centre centre = value.getIdCentre();
		Designation designation = value.getIdDesignation();
		assertNotNull(centre);
		assertNotNull(designation);
		assertEquals(7, centre.getId());
		assertEquals(9, designation.getId());
	}
}
