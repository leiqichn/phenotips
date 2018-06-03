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
package org.phenotips.security.audit.script;

import org.phenotips.Constants;
import org.phenotips.security.audit.AuditEvent;
import org.phenotips.security.audit.AuditStore;
import org.phenotips.security.authorization.AuthorizationService;

import org.xwiki.component.annotation.Component;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.DocumentReferenceResolver;
import org.xwiki.model.reference.EntityReference;
import org.xwiki.script.service.ScriptService;
import org.xwiki.security.authorization.Right;
import org.xwiki.users.UserManager;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Provides access to {@link AuditEvent audit events}.
 *
 * @version $Id$
 * @since 1.4
 */
@Component
@Named("audit")
@Singleton
public class AuditScriptService implements ScriptService
{

    @Inject
    private AuditStore store;

    @Inject
    private UserManager users;

    @Inject
    private AuthorizationService auth;

    @Inject
    @Named("currentmixed")
    private DocumentReferenceResolver<EntityReference> resolver;

    /**
     * Retrieves all the events affecting a specific entity.
     *
     * @param entity a reference to the target entity
     * @return a list of audited events, may be empty
     */
    public List<AuditEvent> getEventsForEntity(DocumentReference entity)
    {
        if (this.auth.hasAccess(this.users.getCurrentUser(), Right.EDIT, entity)) {
            return this.store.getEventsForEntity(entity);
        }
        return Collections.emptyList();
    }

    /**
     * Retrieves all the events generated by a specific user.
     *
     * @param userId the user whose events to retrieve, may be {@code null}
     * @return a list of audited events, may be empty
     */
    public List<AuditEvent> getEventsForUser(String userId)
    {
        if (this.auth.hasAccess(this.users.getCurrentUser(), Right.ADMIN,
            this.resolver.resolve(Constants.XWIKI_SPACE_REFERENCE))) {
            return this.store.getEventsForUser(this.users.getUser(userId));
        }
        return Collections.emptyList();
    }

    /**
     * Retrieves all the events generated by a specific user, coming from a specific IP.
     *
     * @param userId the user whose events to retrieve, may be {@code null}
     * @param ip the ip where the request came from
     * @return a list of audited events, may be empty
     */
    public List<AuditEvent> getEventsForUser(String userId, String ip)
    {
        if (this.auth.hasAccess(this.users.getCurrentUser(), Right.ADMIN,
            this.resolver.resolve(Constants.XWIKI_SPACE_REFERENCE))) {
            return this.store.getEventsForUser(this.users.getUser(userId), ip);
        }
        return Collections.emptyList();
    }
}