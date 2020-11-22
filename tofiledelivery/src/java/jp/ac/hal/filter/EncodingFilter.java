package jp.ac.hal.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class EncodingFilter implements Filter {
	private String encoding = null;

	public void init(FilterConfig config) throws ServletException {
		encoding = config.getInitParameter("encoding");
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
						throws ServletException, IOException {
		req.setCharacterEncoding(encoding);
		res.setContentType("text/html; charset=UTF-8");
		chain.doFilter(req, res);
	}

	public void destroy() {
		encoding = null;
	}
}