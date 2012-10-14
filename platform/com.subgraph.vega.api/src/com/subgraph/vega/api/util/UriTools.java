package com.subgraph.vega.api.util;

import java.net.URI;
import java.net.URISyntaxException;

public class UriTools {
	private final static String[] schemePrefixes = new String[] {"http://", "https://"};
	

	public static boolean isTextValidURI(String input) {
		return getURIFromText(input) != null;
	}
	
	public static URI getURIFromText(String input) {
		try {
			final URI uri = new URI(textToAbsoluteURL(input.trim()));
			if(uri.getHost() == null) {
				return null;
			}
			return uri;
		} catch (URISyntaxException e) {
			return null;
		}
	}
	
	public static boolean doesBaseUriContain(URI base, URI uri) {
		/*
		 * Compare host names to avoid a base scope such as
		 * http://acme.com matching http://acme.computers.net
		 * 
		 * Reparse the URIs because only the 'string' field
		 * in the URI class is repopulated when URI objects
		 * are retrieved from database.
		 * 
		 * XXX create a class VegaURI which persists correctly
		 * and move this method to that class.
		 */
		final URI parsedBase = URI.create(base.toString());
		final URI parsedURI = URI.create(uri.toString());
		final String baseString = parsedBase.toString();
		return parsedBase.getHost().equalsIgnoreCase(parsedURI.getHost()) &&
				uri.toString().startsWith(baseString);
	}


	private static String textToAbsoluteURL(String input) {
		for(String prefix: schemePrefixes) {
			if(input.toLowerCase().startsWith(prefix)) {
				return input;
			}
		}
		return "http://"+ input;
	}
	
	public static URI stripQueryFromUri(URI uri) {
		try {
			return new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), uri.getPath(), null, null);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}
}