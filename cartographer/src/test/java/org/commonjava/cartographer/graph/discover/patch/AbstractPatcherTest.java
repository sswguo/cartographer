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
package org.commonjava.cartographer.graph.discover.patch;

import static org.commonjava.cartographer.INTERNAL.graph.discover.DiscoveryContextConstants.POM_VIEW_CTX_KEY;
import static org.commonjava.cartographer.INTERNAL.graph.discover.DiscoveryContextConstants.TRANSFER_CTX_KEY;

import java.net.URI;
import java.util.*;

import org.commonjava.maven.atlas.graph.rel.ProjectRelationship;
import org.commonjava.maven.atlas.graph.rel.SimpleDependencyRelationship;
import org.commonjava.cartographer.graph.util.RelationshipUtils;
import org.commonjava.maven.atlas.ident.ref.ProjectRef;
import org.commonjava.maven.atlas.ident.ref.ProjectVersionRef;
import org.commonjava.maven.galley.maven.GalleyMavenException;
import org.commonjava.maven.galley.maven.model.view.DependencyView;
import org.commonjava.maven.galley.maven.model.view.MavenPomView;
import org.commonjava.maven.galley.maven.model.view.ProjectRefView;
import org.commonjava.maven.galley.model.Location;
import org.commonjava.maven.galley.model.Transfer;
import org.commonjava.maven.galley.testing.maven.GalleyMavenFixture;
import org.junit.Rule;

public abstract class AbstractPatcherTest
{

    @Rule
    public GalleyMavenFixture galleyFixture = new GalleyMavenFixture();

    protected void setupGalley()
        throws Exception
    {
        galleyFixture.initMissingComponents();
    }

    protected Map<String, Object> getContext( final ProjectVersionRef ref, final Location location )
        throws Exception
    {
        final Transfer txfr = galleyFixture.getArtifactManager()
                                           .retrieve( location, ref.asPomArtifact() );
        final MavenPomView read = galleyFixture.getPomReader()
                                               .read( ref, txfr, Collections.singletonList( location ) );

        final Map<String, Object> ctx = new HashMap<String, Object>();
        ctx.put( POM_VIEW_CTX_KEY, read );
        ctx.put( TRANSFER_CTX_KEY, txfr );

        return ctx;
    }

    protected Set<ProjectRelationship<?, ?>> parseDependencyRelationships( final String pom, final ProjectVersionRef pvr,
                                                                        final Location location, final URI src )
        throws GalleyMavenException
    {
        final Set<ProjectRelationship<?, ?>> discovered = new HashSet<ProjectRelationship<?, ?>>();
        final MavenPomView pomView = galleyFixture.getPomReader()
                                                  .read( pvr, Collections.singletonList( location ) );
        List<DependencyView> deps = pomView.getAllDirectDependencies();
        int idx = 0;
        for ( final DependencyView dep : deps )
        {
            final Set<ProjectRefView> depExclusions = dep.getExclusions();
            final ProjectRef[] depEx;
            if ( depExclusions != null )
            {
                depEx = new ProjectRef[depExclusions.size()];
                int i = 0;
                for ( final ProjectRefView ex : depExclusions )
                {
                    depEx[i++] = ex.asProjectRef();
                }
            }
            else
            {
                depEx = new ProjectRef[0];
            }

            final URI pomLoc = RelationshipUtils.profileLocation( dep.getProfileId() );

            discovered.add( new SimpleDependencyRelationship( src, pomLoc, pvr, dep.asArtifactRef(), dep.getScope(), idx++,
                                                        false, dep.getOriginInfo().isInherited(),
                                                        dep.isOptional(), depEx ) );
        }

        deps = pomView.getAllManagedDependencies();
        idx = 0;
        for ( final DependencyView dep : deps )
        {
            final Set<ProjectRefView> depExclusions = dep.getExclusions();
            final ProjectRef[] depEx;
            if ( depExclusions != null )
            {
                depEx = new ProjectRef[depExclusions.size()];
                int i = 0;
                for ( final ProjectRefView ex : depExclusions )
                {
                    depEx[i++] = ex.asProjectRef();
                }
            }
            else
            {
                depEx = new ProjectRef[0];
            }

            final URI pomLoc = RelationshipUtils.profileLocation( dep.getProfileId() );

            discovered.add( new SimpleDependencyRelationship( src, pomLoc, pvr, dep.asArtifactRef(), dep.getScope(), idx++,
                                                        true, dep.getOriginInfo().isInherited(),
                                                        dep.isOptional(), depEx ) );
        }

        return discovered;
    }

}
