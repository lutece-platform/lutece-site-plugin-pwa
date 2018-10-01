package fr.paris.lutece.plugins.pwa.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.html.HtmlTemplate;

public class PwaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String TEMPLATE_JSON = "/skin/plugins/pwa/manifest.json";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		res.setContentType("text/html; charset=UTF-8");
		Map<String, Object> model = new HashMap<>();

		HtmlTemplate template = AppTemplateService.getTemplate(TEMPLATE_JSON, req.getLocale(), model);

		res.getWriter().write(template.getHtml());

	}

	public void init(ServletConfig config) throws ServletException {
		AppLogService.info("HELLO INIT");
	}

	public String getServletInfo() {
		return "This servlet delivers the manifest.webmanifest file";
	}

	public void destroy() {
	}
}
