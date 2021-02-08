package de.sambalmueslie.openevent.server.common


import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PageableIterator<T>(val pageSize: Int = 20, private val action: (pageable: Pageable) -> Page<T>) : Iterator<T> {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(PageableIterator::class.java)
	}

	private var currentPage: Page<T> = action.invoke(Pageable.from(0, pageSize))
	private var iterator = currentPage.content.iterator()

	override fun hasNext(): Boolean {
		if (iterator.hasNext()) return true
		if (!hasMorePages()) return false
		loadNextPage()
		return iterator.hasNext()
	}

	private fun loadNextPage() {
		currentPage = action.invoke(currentPage.nextPageable())
		iterator = currentPage.content.iterator()
	}

	private fun hasMorePages(): Boolean {
		return currentPage.pageNumber >= currentPage.totalPages
	}

	override fun next(): T {
		return iterator.next()
	}


}
