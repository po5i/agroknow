<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head profile="http://gmpg.org/xfn/11">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>AgroKnow API &raquo; Requests</title>
        <meta name="viewport" content="width=device-width">
    </head>
    <body>
        <div id="viewport" style="width:960px;margin:0 auto;">
            <h1>API</h1>
            <div>
                <h2>Requests</h2>
                <p><a name="url"></a>The base URL of the API is:</p>
                <pre style="padding-left: 30px;"><code>http://keevosh.ath.forthnet.gr:8088/v1</code></pre> (it should change to api.agroknow.com later)
                <h2><a name="types"></a>Resource Types</h2>
                <p>When you formulate a REST query, you have to decide which resource type you want. The resource type is the first word in your query and determines the format of the response data. The only resource type currently offered is described below.</p>

                <h3><code>akif</code></h3>
                <p>AgroKnow <code>akif</code> type is a data model to represent agricultural data.</p>
                <p>The RESTful URL to request data from the akif resource begins:</p>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif</pre>
                <h2>Feature Outline</h2>
                <p>Query features can be broken into two sections: <em>What do you want?</em> and <em>How do you want it?</em></p>
                <h3>What do you want?</h3>
                <ul>
                    <li><a href="#simple">Simple search</a></li>
                    <li><a href="#search-specific-fields">Searching within specific fields</a></li>
                    <li><a href="#temporal">Temporal</a></li>
                    <li><a href="#search-specific-items">Fetching specific items</a></li>
                </ul>
                <h3>How do you want it?</h3>
                <ul>
                    <li><a href="#sorting">Sorting results</a></li>
                    <li><a href="#faceting">Faceting results</a></li>
                    <li><a href="#pagination">Pagination</a></li>
                </ul>
                <h3>Putting it all together</h3>
                <ul>
                    <li><a href="#complex">Complex queries</a></li>
                </ul>
                <p>&nbsp;</p>

                <h2>Features</h2>

                <h3 id="simple">Simple search</h3>
                <p>This is just a keyword search against all the text fields. If don&#8217;t know anything about the fields, but you know you want <code>kittens</code>, use the <code>q</code> parameter.</p>
                <p><strong>Example:</strong></p>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif?q=kittens</pre>
                <p>(items with <code>kittens</code> anywhere in any field)</p>

                <h3 id="search-specific-fields">Searching within specific fields</h3>
                <p>You don&#8217;t know what you want, but you know it has to have <code>yodellers</code> [sic] in the title. It doesn&#8217;t just have to be the title, though. You can search for terms within any textual field <em>except</em>:</p>
                <p>You do not need the <code>q</code> parameter when searching within a specific field. Use the field&#8217;s name as a URL query string parameter instead.</p>
                <p>You can combine specific field searches with simple search. You can also combine multiple field-specific searches. When combining field <code>q</code> parameters, separate them with an ampersand (<code>&amp;</code>). (You already are doing this when you append <code>&amp;api_key</code> to your requests.) These combinations are exclusive, meaning that result items fulfill all simple and field-specific criteria.</p>
                <p><strong>Examples:</strong></p>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif?sourceResource.description=obituaries</pre>
                <p>(items with <code>obituaries</code> anywhere in the <code><a href="http://dp.la/info/developers/codex/field-reference/#sourceResource-description">description</a></code> field)</p>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif?sourceResource.title=yodellers</pre>
                <p>(items with <code>yodellers</code> anywhere in the <code><a href="http://dp.la/info/developers/codex/field-reference/#sourceResource-title">title</a></code> field)</p>
                <p>Multiple:</p>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif?sourceResource.language=Russian&amp;sourceResource.description=1982</pre>
                <p>(items with <code>Russian</code> in the list of <code><a href="http://dp.la/info/developers/codex/field-reference/#sourceResource-language">languages</a></code> and <code>1982</code> in the <code><a href="http://dp.la/info/developers/codex/field-reference/#sourceResource-description">description</a></code>)</p>
                <p>Field &amp; simple:</p>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif?sourceResource.title=zoo&amp;q=zebra</pre>
                <p>(items with <code>zoo</code> in the <code><a href="http://dp.la/info/developers/codex/field-reference/#sourceResource-title">title</a></code> and <code>zebra</code> in any field.)</p>
                <p>Boolean:</p>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif?sourceResource.title=cat+OR+dog</pre>
                <p>(items with <code>cat</code> or <code>dog</code> in the <code><a href="http://dp.la/info/developers/codex/field-reference/#sourceResource-title">title</a></code>.)</p>
                <p>Wildcard:</p>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif?sourceResource.title=yodel*</pre>
                <p>(items with words beginning with <code>yodel</code> in the <code><a href="http://dp.la/info/developers/codex/field-reference/#sourceResource-title">title</a></code>.)</p>

                <h3 id="temporal">Temporal searching</h3>
                <p>The DPLA API understands dates. Combine that with the fielded search above, and you&#8217;ve got a pretty spiffy way of finding records that have fields that fall before, after, and between dates.</p>
                <p>The following examples search the <code><a href="http://dp.la/info/developers/codex/field-reference/#sourceResource-temporal">sourceResource.temporal</a></code> field which is the time frame of the original physical object such as when a book was written, the period about which a book is written, or when a photo was taken.</p>
                <p>This field can be a range of dates, e.g., a book pertaining to the decade of the 1930s can have a begin year of 1930 and an end year of 1939.</p>
                <p><strong>Examples:</strong></p>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif?sourceResource.temporal.begin=1910-01-01</pre>
                <p>(items that were created <a href="http://dp.la/info/developers/codex/field-reference/#sourceResource-temporal-begin">on or after</a> <code>1910</code>)</p>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif?sourceResource.temporal.end=1869-01-01</pre>
                <p>(items that were created <a href="http://dp.la/info/developers/codex/field-reference/#sourceResource-temporal-end">on or before</a> <code>1869</code>)</p>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif?sourceResource.temporal.begin=1910&amp;sourceResource.temporal.end=1920</pre>
                <p>(items that were created between <code>1910</code> &amp; <code>1920</code>)</p>

                <h3 id="search-specific-items">Fetching specific <code>items</code></h3>
                <p>Say you already have the <code><a href="http://dp.la/info/developers/codex/field-reference/#id">id</a></code> for the <code>item</code> you want, and you&#8217;re just looking to get the rest of the metadata for that item. Simply add the <code><a href="http://dp.la/info/developers/codex/field-reference/#id">id</a></code> to the end of the <code>items</code> request. Bonus: You can search for multiple IDs by separating them with a comma (,).</p>
                <p><strong>Example:</strong></p>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif/fffffed915b46b7d71bcc8d888900c4b?api_key=</pre>
                <p>(item with the given <code><a href="http://dp.la/info/developers/codex/field-reference/#id">id</a></code>)</p>

                <h3 id="sorting">Sorting results</h3>
                <p>Sort stuff using the <code>sort_by</code> parameter. We&#8217;ll sort stuff in ascending order by default, but if you&#8217;d like to flip things, set the <code>sort_order</code> parameter to <code>desc</code>.</p>
                <p>To see which fields are sortable, see the <a title="Sortable" href="#sortable">Sortable Fields</a> section.</p>
                <p>You can also sort by distance from a geographic point (which is pretty sweet). Use the <code>sort_by_pin</code> parameter with a latitude and longitude pair, and make sure to specify the coordinates field to use in the <code>sort_by</code> parameter.</p>
                <p><strong>Examples:</strong></p>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif?q=yodeling&amp;sort_by=sourceResource.title</pre>
                <p>(items with <code>yodeling</code> in any field, sorted by the <code><a href="http://dp.la/info/developers/codex/field-reference/#sourceResource-title">title</a></code>)</p>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif?q=yodeling&amp;sort_by=sourceResource.date.begin&amp;sort_order=desc</pre>
                <p>(items with <code>yodeling</code> in any field, sorted by starting time span; most recent items listed first)</p>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif?q=atlanta&amp;sort_by=sourceResource.subject.name</pre>
                <p>(items with <code>atlanta</code> in any field, sorted by the name of their subject)</p>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif?sort_by_pin=42.3,-71&amp;sort_by=sourceResource.spatial.coordinates</pre>
                <p>(all items sorted by distance to Boston)</p>

                <h3 id="faceting">Faceting results</h3>
                <p>Facets tell you the most common values for certain fields in a collection of items. We return a couple different types of facets depending upon the field you&#8217;re looking for. For date fields, we&#8217;ll send back facets of type <code>date_histogram</code> (which is what it sounds like). For complex text fields, we&#8217;ll break it down for you into a <code>terms</code> type. For simple text fields, we&#8217;ll also send back a <code>terms</code> type but with unadulterated values. And for geographic types, we&#8217;ll give you a <code>geo_distance</code> type. See what that looks like in the <a title="Field Reference" href="http://dp.la/info/developers/codex/responses/field-reference/">Field Reference</a>.</p>
                <p>To see which fascinating fields are facetable, see the <a title="Facetable" href="#facetable">Facetable</a>.</p>
                <p><strong>Examples:</strong></p>
                <p>Basic:</p>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif?facets=isPartOf</pre>
                <p>Multiple:</p>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif?facets=sourceResource.publisher,sourceResource.creator</pre>
                <p>No Docs:</p>
                <pre style="padding-left: 30px;">http://api.dp.la/dev/items?facets=aggregatedCHO.date.begin&amp;page_size=0</pre>
                <p>Facet Limit:</p>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif?facets=sourceResource.date.begin&amp;facet_size=3</pre>
                <p>&nbsp;</p>

                <h3 id="pagination">Pagination</h3>
                <p>By default, we&#8217;ll give you 10 items. If that&#8217;s not enough, you can get the next ten items incrementing the <code>page</code> parameter (it&#8217;s one-indexed). If that&#8217;s still not enough, you can pull more items per page by using the <code>page_size</code> parameter (we&#8217;ll limit you to 100 items per page because greediness is a vice).</p>
                <p><strong>Examples:</strong></p>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif?q=yodeling&amp;page_size=2</pre>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif?q=atlanta&amp;page_size=25</pre>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif?q=atlanta&amp;page_size=25&amp;page=3</pre>
                <p>&nbsp;</p>

                <h3 id="complex">Complex Queries</h3>
                <p>Let&#8217;s get this APIarty started.</p>
                <p><strong>Examples:</strong></p>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif?q=atl*&amp;sourceResource.title=africa</pre>
                <p><span>(items with both words beginning with <code>atl</code> in any field and <code>africa</code> in the <code><a href="http://dp.la/info/developers/codex/field-reference/#sourceResource-title">title</a></code>)</span></p>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif?sourceResource.description=atl*+OR+africa&amp;page_size=25</pre>
                <p><span>(the first 25 items with either words beginning with <code>atl</code> or the full word <code>africa</code> in the description)</span></p>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif?sourceResource.title=africa&amp;sourceResource.date.after=1960&amp;sourceResource.date.before=1995&amp;page_size=25</pre>
                <p><span>(the first 25 items with <code>africa</code> in the <code><a href="http://dp.la/info/developers/codex/field-reference/#sourceResource-title">title</a></code> that were published between <a href="http://dp.la/info/developers/codex/field-reference/#sourceResource-date-after">1960</a> and <a href="http://dp.la/info/developers/codex/field-reference/#sourceResource-date-before">1995</a>)</span></p>
                <pre style="padding-left: 30px;">http://keevosh.ath.forthnet.gr:8088/v1/akif?sourceResource.title=africa&amp;sourceResource.date.after=1960&amp;sourceResource.date.before=1995&amp;sort_by=sourceResource.title&amp;page_size=25</pre>
                <p><span>(the first 25 items with <code>africa</code> in the <code><a href="http://dp.la/info/developers/codex/field-reference/#sourceResource-title">title</a></code> that were published between <a href="http://dp.la/info/developers/codex/field-reference/#sourceResource-date-after">1960</a> and <a href="http://dp.la/info/developers/codex/field-reference/#sourceResource-date-before">1995</a> sorted by the <code><a href="http://dp.la/info/developers/codex/field-reference/#sourceResource-title">title</a></code>)</span></p>

                <p>&nbsp;</p>
            </div>
        </div>
    </body>
</html>