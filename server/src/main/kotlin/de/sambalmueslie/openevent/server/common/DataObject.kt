package de.sambalmueslie.openevent.server.common

interface DataObject<T : BusinessObject, C : ConvertContent> {
	fun convert(content: C): T
}
