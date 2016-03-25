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
package com.joliciel.jochre.boundaries;

import com.joliciel.jochre.graphics.Shape;

/**
 * A pair of consecutive shapes.
 * @author Assaf Urieli
 *
 */
public interface ShapePair {
	public Shape getFirstShape();
	public Shape getSecondShape();
	public abstract int getXHeight();
	public abstract int getRight();
	public abstract int getLeft();
	public int getTop();
	public int getBottom();
	public int getWidth();
	public int getHeight();
	public int getInnerDistance();
}