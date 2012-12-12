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

package org.bitbucket.inkytonik.dsprofile;

import scala.Tuple2;

public class JavaProfiler {

    public static void profile (Action action, String... dimensions) {
        ScalaProfiler.profile (action, dimensions);
    }

    public static Tuple2<String,Object> tuple (String s, Object o) {
        return new Tuple2<String,Object> (s, o);
    }

    @SafeVarargs
    public static long start (Tuple2<String,Object>... dimPairs){
        long i = ScalaProfiler.start (dimPairs);
        return i;
    }

    @SafeVarargs
    public static void finish (long i, Tuple2<String,Object>... dimPairs) {
        ScalaProfiler.finish (i, dimPairs);
    }

}
