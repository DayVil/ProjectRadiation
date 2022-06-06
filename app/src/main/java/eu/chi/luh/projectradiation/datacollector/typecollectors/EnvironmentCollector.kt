package eu.chi.luh.projectradiation.datacollector.typecollectors

import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * This is the interface for the collectors of a specific api.
 */
abstract class EnvironmentCollector<T>(protected val _apiKey: String) {
    /** Creates a client to request data. */
    protected val client: OkHttpClient = OkHttpClient()

    /** The url to send to the api. */
    protected lateinit var request: Request

    /**
     * Creates a usable Link to request to.
     *
     * @return returns the link as string
     */
    protected abstract fun makeLink(): String

    /**
     * Collects the data from the api and makes it workable.
     *
     * @return Returns a data class of a generic type.
     */
    abstract fun collect(): T?
}