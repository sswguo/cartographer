/**
 * Copyright (C) 2013 Red Hat, Inc. (jdcasey@commonjava.org)
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
package org.commonjava.cartographer.rest.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.commonjava.cartographer.request.MetadataCollationRequest;
import org.commonjava.cartographer.request.MetadataExtractionRequest;
import org.commonjava.cartographer.request.MetadataUpdateRequest;
import org.commonjava.cartographer.request.ProjectGraphRequest;
import org.commonjava.cartographer.rest.CartoRESTException;
import org.commonjava.cartographer.rest.ctl.MetadataController;
import org.commonjava.cartographer.result.MetadataCollationResult;
import org.commonjava.cartographer.result.MetadataResult;
import org.commonjava.cartographer.result.ProjectListResult;
import org.commonjava.propulsor.deploy.resteasy.RestResources;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static org.commonjava.cartographer.rest.util.ResponseUtils.throwError;
import static org.commonjava.propulsor.deploy.undertow.util.StandardApplicationContent.*;

@Api( value = "Metadata Resource", description = "Metadata Resource." )
@Path( "/api/depgraph/meta" )
@Consumes( { application_json } )
@Produces( { application_json } )
@ApplicationScoped
public class MetadataResource
        implements RestResources
{

    @Inject
    private MetadataController controller;

    @ApiOperation( "Batch Update." )
    @ApiResponses( { @ApiResponse( code = 200, response = ProjectListResult.class, message = "Project List" ) } )
    @Path( "/updates" )
    @POST
    public ProjectListResult batchUpdate( final MetadataUpdateRequest recipe )
    {
        try
        {
            return controller.batchUpdate( recipe );
        }
        catch ( final CartoRESTException e )
        {
            throwError( e );
        }
        return null;
    }

    @ApiOperation( "Rescan." )
    @ApiResponses( { @ApiResponse( code = 200, response = ProjectListResult.class, message = "Project List" ) } )
    @Path( "/rescan" )
    @POST
    public ProjectListResult rescan( final ProjectGraphRequest recipe )
    {
        try
        {
            return controller.rescan( recipe );
        }
        catch ( final CartoRESTException e )
        {
            throwError( e );
        }
        return null;
    }

    @ApiOperation( "Get Metadata." )
    @ApiResponses( { @ApiResponse( code = 200, response = MetadataResult.class, message = "Metadata" ) } )
    @POST
    public MetadataResult getMetadata( final MetadataExtractionRequest recipe )
    {
        try
        {
            return controller.getMetadata( recipe );
        }
        catch ( final CartoRESTException e )
        {
            throwError( e );
        }

        return null;
    }

    @ApiOperation( "Get Collation." )
    @ApiResponses( { @ApiResponse( code = 200, response = MetadataCollationResult.class, message = "Metadata Collation" ) } )
    @Path( "/collation" )
    @POST
    public MetadataCollationResult getCollation( final MetadataCollationRequest recipe )
    {
        try
        {
            return controller.getCollation( recipe );
        }
        catch ( final CartoRESTException e )
        {
            throwError( e );
        }

        return null;
    }
}
