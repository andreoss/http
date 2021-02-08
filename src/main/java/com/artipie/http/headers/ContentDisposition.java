/*
 * MIT License
 *
 * Copyright (c) 2020 Artipie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.artipie.http.headers;

import com.artipie.http.Headers;
import com.artipie.http.rq.RqHeaders;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Content-Disposition header.
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Disposition"></a>
 * @since 0.17.8
 */
public final class ContentDisposition extends Header.Wrap {

    /**
     * Header name.
     */
    public static final String NAME = "Content-Disposition";

    /**
     * Header directives.
     */
    private static final Pattern DIRECTIVES = Pattern.compile(
        "(?<key> \\w+ ) (?:= [\"] (?<value> [^\"]+ ) [\"] )?[;]?",
        Pattern.COMMENTS
    );

    /**
     * Ctor.
     *
     * @param value Header value.
     */
    public ContentDisposition(final String value) {
        super(new Header(ContentDisposition.NAME, value));
    }

    /**
     * Ctor.
     *
     * @param headers Headers to extract header from.
     */
    public ContentDisposition(final Headers headers) {
        this(new RqHeaders.Single(headers, ContentDisposition.NAME).asString());
    }

    /**
     * The original name of the file transmitted.
     *
     * @return String.
     */
    public String fileName() {
        return this.values().get("filename");
    }

    /**
     * The name of the HTML field in the form
     * that the content of this subpart refers to.
     *
     * @return String.
     */
    public String fieldName() {
        return this.values().get("name");
    }

    /**
     * Inline.
     *
     * @return Boolean flag.
     */
    public Boolean isInline() {
        return this.values().containsKey("inline");
    }

    /**
     * Inline.
     *
     * @return Boolean flag.
     */
    public Boolean isAttachment() {
        return this.values().containsKey("attachment");
    }

    /**
     * Parse header value to a map.
     *
     * @return Map of keys and values.
     */
    private Map<String, String> values() {
        final Map<String, String> values = new HashMap<>();
        final String value = this.getValue();
        final Matcher matcher = ContentDisposition.DIRECTIVES.matcher(value);
        while (matcher.find()) {
            values.put(matcher.group("key"), matcher.group("value"));
        }
        return values;
    }
}