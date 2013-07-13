package jp.niconico.api.method;

import java.util.List;

import jp.niconico.api.entity.RankingInfo;
import jp.niconico.api.exception.NiconicoException;
import jp.niconico.api.http.HttpClientSetting;
import jp.niconico.api.method.constant.RankingKind;
import jp.niconico.api.method.constant.RankingPeriod;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class NicoGetRanking {
	private String methodUrl = "http://www.nicovideo.jp/ranking/";

	public List<RankingInfo> execute(RankingPeriod period, RankingKind kind)
			throws NiconicoException {

		DefaultHttpClient httpClient = null;
		List<RankingInfo> rankingList = null;
		try {
			StringBuilder url = new StringBuilder(methodUrl);
			url.append(kind.toString());
			url.append("/");
			url.append(period.toString());
			url.append("/");
			url.append("all?rss=2.0");

			httpClient = HttpClientSetting.createHttpClient();
			HttpGet httpGet = new HttpGet(url.toString());
			HttpResponse response = httpClient.execute(httpGet);

			String xml = EntityUtils.toString(response.getEntity());
			rankingList = RankingInfo.parse(period, kind, xml);

		} catch (Exception e) {
			throw new NiconicoException(e);
		} finally {
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
			}
		}

		return rankingList;
	}
}
