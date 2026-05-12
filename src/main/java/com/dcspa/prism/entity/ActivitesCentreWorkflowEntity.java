package com.dcspa.prism.entity;

public interface ActivitesCentreWorkflowEntity {
	Boolean getValideeCoordonnateur();

	void setValideeCoordonnateur(Boolean valideeCoordonnateur);

	Boolean getValideeSuperviseur();

	void setValideeSuperviseur(Boolean valideeSuperviseur);

	Boolean getValideeCentrale();

	void setValideeCentrale(Boolean valideeCentrale);
}
