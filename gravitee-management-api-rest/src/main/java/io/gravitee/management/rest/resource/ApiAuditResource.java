/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.management.rest.resource;

import io.gravitee.common.data.domain.MetadataPage;
import io.gravitee.common.http.MediaType;
import io.gravitee.management.model.audit.AuditEntity;
import io.gravitee.management.model.audit.AuditQuery;
import io.gravitee.management.model.permissions.RolePermission;
import io.gravitee.management.model.permissions.RolePermissionAction;
import io.gravitee.management.rest.resource.param.AuditParam;
import io.gravitee.management.rest.security.Permission;
import io.gravitee.management.rest.security.Permissions;
import io.gravitee.management.service.AuditService;
import io.gravitee.repository.management.model.Audit;
import io.swagger.annotations.Api;
import org.reflections.Reflections;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 * @author Nicolas GERAUD (nicolas.geraud at graviteesource.com)
 * @author GraviteeSource Team
 */
@Api(tags = {"API"})
public class ApiAuditResource extends AbstractResource {

    private static final List<Audit.AuditEvent> events = new ArrayList<>();

    @Inject
    private AuditService auditService;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Permissions({
            @Permission(value = RolePermission.API_AUDIT, acls = RolePermissionAction.READ)
    })
    public MetadataPage<AuditEntity> list(@PathParam("api") String api,
                                          @BeanParam AuditParam param) {

        AuditQuery query = new AuditQuery();
        query.setFrom(param.getFrom());
        query.setTo(param.getTo());
        query.setPage(param.getPage());
        query.setSize(param.getSize());
        query.setApiIds(Collections.singletonList(api));
        query.setApplicationIds(Collections.emptyList());
        query.setManagementLogsOnly(false);

        if (param.getEvent() != null) {
            query.setEvents(Collections.singletonList(param.getEvent()));
        }

        return auditService.search(query);
    }

    @Path("/events")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Permissions({
            @Permission(value = RolePermission.API_AUDIT, acls = RolePermissionAction.READ)
    })
    public Response getEvents() {
        if (events.isEmpty()) {
            Set<Class<? extends Audit.ApiAuditEvent>> subTypesOf =
                    new Reflections("io.gravitee.repository.management.model")
                            .getSubTypesOf(Audit.ApiAuditEvent.class);
            for (Class<? extends Audit.ApiAuditEvent> clazz : subTypesOf) {
                if (clazz.isEnum()) {
                    events.addAll(Arrays.asList(clazz.getEnumConstants()));
                }
            }

            events.sort(Comparator.comparing(Audit.AuditEvent::name));
        }
        return Response.ok(events).build();
    }
}
