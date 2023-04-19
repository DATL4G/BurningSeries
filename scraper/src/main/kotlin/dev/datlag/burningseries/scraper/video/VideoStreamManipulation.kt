package dev.datlag.burningseries.scraper.video

import dev.datlag.burningseries.model.HosterStream
import org.jsoup.nodes.Document

interface VideoStreamManipulation {

    fun match(hosterStream: HosterStream): Boolean

    fun find(doc: Document): List<String>

    fun remove(source: List<String>): List<String>

    fun headers(source: Map<String, String>, hosterStream: HosterStream): Map<String, String> = source
}