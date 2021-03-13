package de.sambalmueslie.openevent.server.common

interface DataObject<T : BusinessObject, C : ConvertContent> : BusinessObject {
	fun convert(content: C): T
}
