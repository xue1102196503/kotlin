// !WITH_NEW_INFERENCE
// See KT-15566
// NI_EXPECTED_FILE

import DefaultHttpClient.client

interface HttpClient

class HttpClientImpl : HttpClient

// Below we should have initialization error for both (!) delegates

object DefaultHttpClient : HttpClient by <!NI;UNINITIALIZED_VARIABLE!><!UNINITIALIZED_VARIABLE!>client<!><!> {
    val client = HttpClientImpl()
}

object DefaultHttpClientWithGetter : HttpClient by client {
    val client get() = HttpClientImpl()
}

object DefaultHttpClientWithFun : HttpClient by fClient() {
}

private fun fClient() = HttpClientImpl()

private fun <T> lazy(init: () -> T): kotlin.<!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>Lazy<!><!><T> {
    init()
    null!!
}

object DefaultHttpClientWithBy : HttpClient by client {
    val client by <!NI;DELEGATE_SPECIAL_FUNCTION_MISSING!>lazy { HttpClientImpl() }<!>
}

object DefaultFqHttpClient : HttpClient by DefaultFqHttpClient.<!NI;UNINITIALIZED_VARIABLE!><!UNINITIALIZED_VARIABLE!>client<!><!> {
    val client = HttpClientImpl()
}