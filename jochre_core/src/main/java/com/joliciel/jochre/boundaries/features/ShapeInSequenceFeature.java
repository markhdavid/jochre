///////////////////////////////////////////////////////////////////////////////
//Copyright (C) 2012 Assaf Urieli
//
//This file is part of Jochre.
//
//Jochre is free software: you can redistribute it and/or modify
//it under the terms of the GNU Affero General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//Jochre is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU Affero General Public License for more details.
//
//You should have received a copy of the GNU Affero General Public License
//along with Jochre.  If not, see <http://www.gnu.org/licenses/>.
//////////////////////////////////////////////////////////////////////////////
package com.joliciel.jochre.boundaries.features;

import com.joliciel.talismane.machineLearning.features.Feature;

/**
 * A feature defining a shape within a ShapeSequence. Typically only required if
 * the shape's position in the sequence or the other shapes in the sequence are
 * concerned by the feature.
 * 
 * @author Assaf Urieli
 *
 */
public interface ShapeInSequenceFeature<Y> extends Feature<ShapeInSequenceWrapper, Y> {

}
