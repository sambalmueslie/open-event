package de.sambalmueslie.openevent.server.common

interface Action<T : BusinessObject> : BusinessObjectChangeListener<T> {
}
