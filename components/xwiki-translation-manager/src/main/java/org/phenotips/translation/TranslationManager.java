/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/
 */
package org.phenotips.translation;

import org.xwiki.component.annotation.Component;
import org.xwiki.localization.LocalizationContext;
import org.xwiki.localization.LocalizationManager;
import org.xwiki.localization.Translation;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.renderer.BlockRenderer;
import org.xwiki.rendering.renderer.printer.DefaultWikiPrinter;
import org.xwiki.rendering.renderer.printer.WikiPrinter;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @version $Id$
 */
@Component(roles = { TranslationManager.class })
@Singleton
public final class TranslationManager
{
    @Inject
    private static LocalizationManager localizationManager;

    @Inject
    private static LocalizationContext localizationContext;

    /** Renders content blocks into plain strings. */
    @Inject
    @Named("plain/1.0")
    private static BlockRenderer renderer;

    private TranslationManager()
    {
    }

    /**
     * Translate a key into a message based on the locale.
     *
     * @param key of message
     * @return locale based message
     */
    public static String translate(String key)
    {
        Locale currentLocale = TranslationManager.localizationContext.getCurrentLocale();
        Translation translation = TranslationManager.localizationManager.getTranslation(key, currentLocale);
        if (translation == null) {
            return "";
        }
        Block block = translation.render(TranslationManager.localizationContext.getCurrentLocale());

        // Render the block
        WikiPrinter wikiPrinter = new DefaultWikiPrinter();
        TranslationManager.renderer.render(block, wikiPrinter);

        return wikiPrinter.toString();
    }
}
