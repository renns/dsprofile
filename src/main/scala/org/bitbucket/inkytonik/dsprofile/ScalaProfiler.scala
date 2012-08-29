/**
 * This file is part of dsprofile.
 *
 * Copyright (C) 2012 Anthony M Sloane, Macquarie University.
 *
 * dsprofile is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * dsprofile is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with dsprofile.  (See files COPYING and COPYING.LESSER.)  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.bitbucket.inkytonik.dsprofile

object ScalaProfiler extends Profiler {

    def profile (action : Action, dimensions : Array[String]) {
        val func = new ActionFunction (action)
        super.profile (func (), dimensions.toSeq : _*)
    }

    type DimPair = Tuple2[String,Any]

    def start (dimPairs : Array[DimPair]) {
        Events.start (dimPairs : _*);
    }

    def finish (dimPairs : Array[DimPair]) {
        Events.finish (dimPairs : _*);
    }

}
