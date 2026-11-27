/*
 * Copyright (c) 2002-2026, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */

package fr.paris.lutece.plugins.pwa.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.pwa.service.ManifestBuilder;
import fr.paris.lutece.plugins.pwa.service.PwaImage;
import fr.paris.lutece.plugins.pwa.service.PwaImageService;
import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.includes.PageInclude;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.template.HtmlMarkup;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Puts in the page, under the mark pwa_head, everything the head of a page needs for the progressive web app: the manifest link, the theme color, the iOS
 * tags, the styles and the script that registers the service worker, offers the installation and announces the updates
 */
public class PwaHeadInclude implements PageInclude
{
    public static final String MARK_HEAD = "pwa_head";
    private static final String TEMPLATE_HEAD = "/skin/plugins/pwa/pwa_head.html";
    private static final String MARK_THEME_COLOR = "theme_color";
    private static final String MARK_APP_TITLE = "app_title";
    private static final String MARK_TOUCH_ICON = "touch_icon";
    private static final String ICON_TOUCH = "icon-192";
    private static final String DEFAULT_TOUCH_ICON = "themes/shared/plugins/pwa/images/icon-192.png";

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillTemplate( Map<String, Object> rootModel, PageData data, int nMode, HttpServletRequest request )
    {
        // The search indexer builds the pages without request: the head of the application is no content to index
        if ( request == null )
        {
            return;
        }

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_THEME_COLOR, ManifestBuilder.toCssColor( DatastoreService.getDataValue( ManifestBuilder.KEY_THEME_COLOR, null ), StringUtils.EMPTY ) );
        model.put( MARK_APP_TITLE, StringUtils.trimToEmpty( StringUtils.defaultIfBlank( DatastoreService.getDataValue( ManifestBuilder.KEY_SHORT_NAME, null ),
                DatastoreService.getDataValue( ManifestBuilder.KEY_NAME, null ) ) ) );

        PwaImage touchIcon = CDI.current( ).select( PwaImageService.class ).get( ).getIcons( ).get( ICON_TOUCH );
        model.put( MARK_TOUCH_ICON, touchIcon != null ? PwaImageService.getUrl( request.getContextPath( ), touchIcon ) : DEFAULT_TOUCH_ICON );

        rootModel.put( MARK_HEAD, HtmlMarkup.of( AppTemplateService.getTemplate( TEMPLATE_HEAD, LocaleService.getContextUserLocale( request ), model ).getHtml( ) ) );
    }
}
