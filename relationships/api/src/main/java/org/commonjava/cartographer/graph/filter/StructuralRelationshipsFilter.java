/**
 * Copyright (C) 2012 Red Hat, Inc. (jdcasey@commonjava.org)
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
package org.commonjava.cartographer.graph.filter;

import static org.commonjava.maven.atlas.graph.rel.RelationshipType.BOM;
import static org.commonjava.maven.atlas.graph.rel.RelationshipType.PARENT;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.commonjava.maven.atlas.graph.rel.ProjectRelationship;
import org.commonjava.maven.atlas.graph.rel.RelationshipType;

public final class StructuralRelationshipsFilter
    extends AbstractTypedFilter
{

    private static final Collection<RelationshipType> TYPES =
        Collections.unmodifiableCollection( Arrays.asList( PARENT, BOM ) );

    private static final long serialVersionUID = 1L;

    public static final StructuralRelationshipsFilter INSTANCE = new StructuralRelationshipsFilter();

    private StructuralRelationshipsFilter()
    {
        super( TYPES, TYPES, false, true );
    }

    @Override
    public ProjectRelationshipFilter getChildFilter( final ProjectRelationship<?, ?> parent )
    {
        return this;
    }

}
