package com.dcspa.prism.controller.support;

import org.hibernate.proxy.HibernateProxy;

import java.lang.reflect.Method;

/**
 * Lit l’identifiant d’une association JPA sans initialiser un proxy Hibernate
 * (évite {@link org.hibernate.LazyInitializationException} hors session).
 */
public final class JpaAssociationIds {

	private JpaAssociationIds() {
	}

	public static Integer intIdOrNull(Object association) {
		if (association == null) {
			return null;
		}
		if (association instanceof HibernateProxy hp) {
			return toInteger(hp.getHibernateLazyInitializer().getIdentifier());
		}
		try {
			Method m = association.getClass().getMethod("getId");
			Object id = m.invoke(association);
			return toInteger(id);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("Cannot read id from " + association.getClass().getName(), e);
		}
	}

	private static Integer toInteger(Object id) {
		if (id == null) {
			return null;
		}
		if (id instanceof Integer i) {
			return i;
		}
		if (id instanceof Number n) {
			return n.intValue();
		}
		return null;
	}
}
