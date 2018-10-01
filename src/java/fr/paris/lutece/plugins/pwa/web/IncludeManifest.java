package fr.paris.lutece.plugins.pwa.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.includes.PageInclude;

public class IncludeManifest implements PageInclude {

	private static final String MARK_MY_INCLUDE = "pwa_link";

	/**
     * {@inheritDoc }
     */
     @Override
     public void fillTemplate(Map<String, Object> rootModel, PageData data, int nMode, HttpServletRequest request) {
           rootModel.put(MARK_MY_INCLUDE , "<link href=\"servlet/plugins/pwa/PwaServlet\" rel=\"manifest\">");
     }
}
