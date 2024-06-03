package dev.datlag.burningseries.network.common

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.parser.Parser
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.isSuccess

suspend fun Ksoup.parseGet(
    url: String,
    client: HttpClient,
    parser: Parser = Parser.htmlParser()
): Document? {
    val httpResponse = client.get(url)
    if (!httpResponse.status.isSuccess()) {
        return null
    }

    val finalUrl = httpResponse.request.url.toString()
    val response = httpResponse.bodyAsText()
    return parse(
        html = response,
        parser = parser,
        baseUri = finalUrl
    )
}

fun Element.title(): String? {
    return this.attr("title").ifBlank { null } ?: this.text().ifBlank { null }
}

fun Element.href(): String? {
    return this.attr("href").ifBlank { null }
}

fun Element.src(): String? {
    return this.attr("src").ifBlank { null } ?: run {
        val sources = this.select("source")
        sources.firstOrNull()?.src()
    }
}