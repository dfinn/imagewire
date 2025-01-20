package net.dfinn.imagewire.domain

/**
 * Returns true if the search query contains at least one non-whitespace character.
 **/
class ValidateQueryTextUseCase {
    operator fun invoke(query: String): Boolean = query.isNotBlank()
}