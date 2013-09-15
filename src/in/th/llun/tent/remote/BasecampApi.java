package in.th.llun.tent.remote;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

public class BasecampApi extends DefaultApi20 {

	private static final String BASECAMP_AUTHORIZE_URL = "https://launchpad.37signals.com/authorization/new?type=user_agent&client_id=%s&redirect_uri=%s";
	private static final String BASECAMP_TOKEN_URL = "https://launchpad.37signals.com/authorization/token?type=user_agent";

	@Override
	public Verb getAccessTokenVerb() {
		return Verb.POST;
	}

	@Override
	public String getAccessTokenEndpoint() {
		return BASECAMP_TOKEN_URL;
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		Preconditions.checkValidUrl(config.getCallback(),
		    "Must provide a valid url as callback. Basecamp does not support OOB");

		return String.format(BASECAMP_AUTHORIZE_URL, config.getApiKey(),
		    OAuthEncoder.encode(config.getCallback()));
	}

}
