
package cn.rxframework.utils.rest.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class HttpGetUtils {

	private static Logger log = LoggerFactory.getLogger(HttpGetUtils.class);

	private static final String USER_AGENT_IE = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727)";
	private static final String USER_AGENT_GOOGLE = "GoogleBot";
	private static final String USER_AGENT_BAIDU = "BaiDuSpider";
	private static final String USER_AGENT_YAHOO = "Inktomi Slurp";

	private HttpClient client;

	private HttpClientParams hparas;

	private List httpProxyList;

	private List<Header> headers;

	private boolean isRandomIp = false;

	private boolean isSipiderAgent = false;

	private int defaultTimeOut = 20000;

	public HttpGetUtils() {
		this.hparas = new HttpClientParams();
		this.hparas.setSoTimeout(defaultTimeOut);
		this.hparas.setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		this.hparas.setBooleanParameter("http.protocol.single-cookie-header", true);

		this.client = new HttpClient(hparas, new SimpleHttpConnectionManager(true));
		client.setParams(hparas);
	}

	public HttpGetUtils(List<Header> headers) {
		this.headers = headers;

		this.hparas = new HttpClientParams();
		this.hparas.setSoTimeout(defaultTimeOut);
		this.hparas.setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		this.hparas.setBooleanParameter("http.protocol.single-cookie-header", true);

		this.client = new HttpClient(hparas, new SimpleHttpConnectionManager(true));
		client.setParams(hparas);
	}

	public HttpGetUtils(List<Header> headers, int _timeout) {
		this.headers = headers;

		this.hparas = new HttpClientParams();
		this.hparas.setSoTimeout(_timeout);
		this.hparas.setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		this.hparas.setBooleanParameter("http.protocol.single-cookie-header", true);

		this.client = new HttpClient(hparas, new SimpleHttpConnectionManager(true));
		client.setParams(hparas);
	}

	public HttpGetUtils(int _timeout) {
		this.hparas = new HttpClientParams();
		this.hparas.setSoTimeout(_timeout);
		this.hparas.setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		this.hparas.setBooleanParameter("http.protocol.single-cookie-header", true);

		this.client = new HttpClient(hparas, new SimpleHttpConnectionManager(true));
		client.setParams(hparas);
	}

	public HttpGetUtils(int _timeout, boolean _isRandomIp, boolean _isSipiderAgent) {
		this.isSipiderAgent = _isSipiderAgent;
		this.isRandomIp = _isRandomIp;

		this.hparas = new HttpClientParams();
		this.hparas.setSoTimeout(_timeout);
		this.hparas.setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		this.hparas.setBooleanParameter("http.protocol.single-cookie-header", true);

		this.client = new HttpClient(hparas, new SimpleHttpConnectionManager(true));
		client.setParams(hparas);
	}

	public HttpGetUtils(String proxyIP, int port) {
		this.hparas = new HttpClientParams();
		this.hparas.setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		this.hparas.setBooleanParameter("http.protocol.single-cookie-header", true);

		this.client = new HttpClient(hparas, new SimpleHttpConnectionManager(true));
		client.setParams(hparas);
		client.getHostConfiguration().setProxy(proxyIP, port);
	}

	public void addCookie(Cookie cookie) {
		client.getState().addCookie(cookie);
	}

	/**
	 *
	 如果你要发送多个cookie，其实可以这样发的： state.addCookies (new Cookie[]{ new
	 * Cookie(" www.aaa.com","popped","yes","/",new Date(2006,12,8),false), new
	 * Cookie(" www.aaa.com","rtime","2","/",new Date(2006,12,8),false), new
	 * Cookie(" www.aaa.com","ltime","1149940477953","/",new
	 * Date(2006,12,8),false), new Cookie(" www.aaa.com ","cnzz02","1","/",new
	 * Date(2006,12,8),false), });
	 * 但是截包就会发现，httpclient会在header里构件多个cookie项，每一项只含有一个cookie
	 * ，这同IE是不一样的。IE和Firefox会把所有的cookie打包成一个，然后在这个cookie里按照分号把每一项隔开，中间有个空格。
	 * 所以如果用httpclient，还想让cookie正常的话，请使用下面这种形式： String cookies =
	 * "yes; rtime=2; ltime=1149940477953; cnzz02=1"; state.addCookie(new
	 * Cookie("blog.aaa.com","poped",cookies,"/",new Date(2006,12,8),false));
	 */
	public void addCookies(Cookie[] cookie) {
		client.getState().addCookies(cookie);
	}

	/**
	 * 用户自定义header添加
	 */
	public void setRequestHeader(HttpMethod method) {
		if(CollectionUtils.isNotEmpty(headers)){
			for (Header header : headers) {
				method.addRequestHeader(header);
			}
		}
	}

	public HttpClient login(String target, Map parameters, String methodtype) {
		String redirectLocation = "";
		HttpMethod method = getHttpMethod(target, methodtype, parameters);
		try {
			setRequestHeader(method);
			this.client.executeMethod(method);
			redirectLocation = getRedirectLoacation(method);
			if (httpIsRedirect(redirectLocation)) {
				method.releaseConnection();
				return login(redirectLocation, parameters, methodtype);
			}
			return this.client;
		} catch (HttpException e) {
			log.error("", e);
			return null;
		} catch (IOException e) {
			log.error("", e);
			return null;
		} finally {
			method.releaseConnection();
		}

	}

	private boolean httpIsRedirect(String redirectLocation) {
		boolean isredirect = false;
		if (redirectLocation != null && !redirectLocation.trim().equals("")
				&& redirectLocation.toLowerCase().startsWith("http")) {
			isredirect = true;

		}

		return isredirect;
	}

	private String getRedirectLoacation(HttpMethod method) {
		String redirectLocation = "";
		Header locationHeader = null;
		locationHeader = method.getResponseHeader("location");
		if (locationHeader != null) {
			redirectLocation = locationHeader.getValue();
		}
		return redirectLocation;

	}

	private void addPostParameter(HttpMethod method, Map parameters) {

		if (parameters != null && !parameters.isEmpty()) {
			Set keySet = parameters.keySet();
			Iterator keys = keySet.iterator();
			int i = 0;
			org.apache.commons.httpclient.NameValuePair parameter;
			for (; keys.hasNext(); ((PostMethod) method)
					.addParameter(parameter)) {
				Object key = keys.next();
				Object val = parameters.get(key);
				parameter = new org.apache.commons.httpclient.NameValuePair(key.toString(), val.toString());
			}

		}
	}

	private String transTargetByGet(String target, Map parameters) {

		if (parameters != null && !parameters.isEmpty()) {
			Set keySet = parameters.keySet();
			Iterator keys = keySet.iterator();

			while (keys.hasNext()) {
				Object key = keys.next();
				Object val = parameters.get(key);
				String p1 = "?" + key + "=" + val;
				String p2 = "&" + key + "=" + val;
				if (target.indexOf(p1) == -1 && target.indexOf(p2) == -1) {
					if (target.indexOf("?") == -1) {
						target += "?" + key + "=" + val;
					} else {
						target += "&" + key + "=" + val;
					}
				}
			}
		}
		return target;
	}

	private InputStream getHttpInputStream(HttpClient client,
										   HttpMethod method, Map parameters, String USER_AGENT) {
		InputStream input = null;
		String redirectLocation = "";

		// method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
		// new DefaultHttpMethodRetryHandler());

		String[] proxyObj = null;

		// GoogleBot
		// client.getHostConfiguration().setProxy("195.175.37.6", 8080);

		method.setRequestHeader("GET", "/ HTTP/1.1");
		method.setRequestHeader("Accept", "*/*");
		method.setRequestHeader("Accept-Language", "zh-cn");
		method.setRequestHeader("Accept-Encoding", "gzip, deflate");
		// method.setRequestHeader("User-Agent",
		// "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727)");
		method.setRequestHeader("Connection", "Keep-Alive");
		method.setRequestHeader("User-Agent", USER_AGENT);

		setRequestHeader(method);

		// if(this.isSipiderAgent){
		// method.setRequestHeader("User-Agent", userAgent[(int)(Math.random() *
		// (double)userAgent.length)]);
		// System.out.println(method.getRequestHeader("User-Agent"));
		// }else{
		// method.setRequestHeader("User-Agent",
		// "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .JAVA CLR 2.0.50727)");
		// }
		//
		// while(proxyObj == null && this.isRandomIp){
		// try{
		// proxyObj = (String[])httpProxyList.get((int)(Math.random() *
		// (double)httpProxyList.size()));
		// client.getHostConfiguration().setProxy(proxyObj[0],Integer.parseInt(proxyObj[1]));
		// System.out.println("user ip:" + proxyObj[0] + " port:" +
		// proxyObj[1]);
		// }catch(Exception e){
		// System.out.println("随机取得IP出错,总共有IP数量:" + httpProxyList.size());
		// try {
		// Thread.sleep(2000);
		// } catch (InterruptedException e1) {
		// e1.printStackTrace();
		// }
		// }
		// }

		try {
			// 执行Method
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				log.error("Method failed: "
						+ method.getStatusLine());
			}

			// 读取内容
			redirectLocation = getRedirectLoacation(method);
			if (httpIsRedirect(redirectLocation)) {
				method.releaseConnection();
				return getHttpInputStream(
						client,
						getHttpMethod(redirectLocation, method.getName(),
								parameters), parameters, USER_AGENT);
			}

			Header header = method.getResponseHeader("Content-Encoding");
			boolean isGzip = false;
			String value = null;
			if (header != null) {
				value = header.getValue();
				if (value.equals("gzip")) {
					isGzip = true;
				}
			}

			input = method.getResponseBodyAsStream();

			if (isGzip) {
				input = new GZIPInputStream(input);
			}

		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			log.error("", e);
			// log.error("", e);
		} catch (IOException e) {
			// 发生网络异常
			// log.error("", e);
			log.error("", e);
		}
		// finally {
		// // 释放连接
		// method.releaseConnection();
		// }
		return input;

	}

	private byte[] getBodyByte(HttpClient client, HttpMethod method, String USER_AGENT) {

		method.setRequestHeader("GET", "/ HTTP/1.1");
		method.setRequestHeader("Accept", "*/*");
		method.setRequestHeader("Accept-Language", "zh-cn");
		method.setRequestHeader("Accept-Encoding", "gzip, deflate");
		// method.setRequestHeader("User-Agent",
		// "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727)");
		method.setRequestHeader("Connection", "Keep-Alive");
		method.setRequestHeader("User-Agent", USER_AGENT);

		setRequestHeader(method);

		byte[] body = null;
		try {
			// 执行Method
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				log.error("Method failed: " + method.getStatusLine());
			}

			Header header = method.getResponseHeader("Content-Encoding");
			boolean isGzip = false;
			String value = null;
			if (header != null) {
				value = header.getValue();
				if (value.equals("gzip")) {
					isGzip = true;
				}
			}
			body = method.getResponseBody();

		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			log.error("", e);
		} catch (IOException e) {
			// 发生网络异常
			log.error("", e);
		} finally{
			method.releaseConnection();
		}
		return body;
	}

	private String getBodyToString(HttpClient client, HttpMethod method,
								   Map parameters, String encoding, String USER_AGENT) {
		String body = "";

		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encoding);
		InputStream input = getHttpInputStream(client, method, parameters, USER_AGENT);

		try {
			if(input != null){
				body = IOUtils.toString(input, encoding);
			}else{
				log.error("getBodyToString error >> input: " + input);
				log.error("getBodyToString error >> path: " + method.getPath());
				log.error("getBodyToString error >> parameters: " + JSON.toJSONString(parameters));
			}
		} catch (IOException e) {
			// 发生网络异常
			// log.error("", e);
			log.error("", e);
		} finally {
			// 释放连接
			method.releaseConnection();
		}

		return body;
	}

	private Object[] getBody(HttpClient client, HttpMethod method, Map parameters, String encoding) {

		try {
			String body = "";
			setRequestHeader(method);
			body = getBodyToString(client, method, parameters, encoding,
					USER_AGENT_IE);

			Timestamp lastModified = new Timestamp(System.currentTimeMillis());
			Header headers[] = method.getResponseHeaders();
			for (int i = 0; i < headers.length; i++) {
				if (!headers[i].getName().equals("Last-Modified"))
					continue;
				try {
					SimpleDateFormat df = new SimpleDateFormat(
							"d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
					lastModified = new Timestamp(df.parse(
							headers[i].getValue().substring(5)).getTime());
				} catch (Exception e) {
					log.error("", e);
				}
				break;
			}

			return (new Object[] { body, lastModified });
		} catch (Exception e) {
			log.error("", e);
		}
		return null;
	}

	private HttpMethod getHttpMethod(String url, String methodtype,
									 Map parameters) {
		HttpMethod method = null;
		if (methodtype.toLowerCase().equals("get")) {
			url = transTargetByGet(url, parameters);
			method = new GetMethod(url);
		} else if (methodtype.toLowerCase().equals("post")) {
			method = new PostMethod(url);
			addPostParameter(method, parameters);
		}
		return method;
	}

	private HttpMethod getHttpGetMethod(String baseUrl,
										List<NameValuePair> params, String encode) throws ParseException,
			UnsupportedEncodingException, IOException {
		String fullGetUrl = makeFullGetUrl(baseUrl, params, encode);
		return new GetMethod(fullGetUrl);
	}

	private String makeFullGetUrl(String baseUrl, List<NameValuePair> params,
								  String encode) throws ParseException, UnsupportedEncodingException,
			IOException {
		String url = baseUrl;
		if (CollectionUtils.isNotEmpty(params)) {
			String queryString = EntityUtils.toString(new UrlEncodedFormEntity(
					params, encode));
			url = baseUrl + "?" + queryString;
		}
		return url;
	}

	public String getBodyToString(String url, String encoding, Map parameters,
								  String methodtype) {
		return getBodyToString(this.client,
				getHttpMethod(url, methodtype, parameters), parameters,
				encoding, USER_AGENT_IE);
	}


	public String getBodyToString(String url, String encoding, List<NameValuePair> params) throws ParseException, UnsupportedEncodingException, IOException {
		HttpMethod getMethod = getHttpGetMethod(url, params, encoding);
		return getBodyToString(this.client,
				getMethod, null, encoding, USER_AGENT_IE);
	}


	public String getBodyToString(String url, String encoding, Map parameters,
								  String methodtype, String USER_AGENT) {
		return getBodyToString(this.client,
				getHttpMethod(url, methodtype, parameters), parameters,
				encoding, USER_AGENT);
	}

	public Object[] getBody(String url, String encoding, Map parameters, String methodtype) {
		return getBody(this.client, getHttpMethod(url, methodtype, parameters), parameters, encoding);
	}

	/**
	 * 文件获取
	 * @param url
	 * @throws IOException
	 * @throws HttpException
	 */
	public byte[] getBody(String url) throws HttpException, IOException {
		GetMethod method = new GetMethod(url);
		return getBodyByte(this.client, method, USER_AGENT_IE);
	}

}