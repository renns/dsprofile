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

object Events {

    import java.lang.System.nanoTime
    import scala.collection.mutable.ArrayBuffer

    /**
     * Flag to control whether profiling data is stored or not. When this
     * is false (the default) the event generation routines `start` and
     * `finish` can be called, but will not store anything. If you want
     * profiling, you need to turn this flag on explicitly, or by calling
     * the `Profiler.profiler` method which will turn the flag on while
     * a profiled computation is being evaluated.
     */
    var profiling = false

    /**
     * The type of a dimension name.
     */
    type Dimension = String

    /**
     * The type of a dimension value.
     */
    type Value = Any

    /**
     * Type of collection of event dimension names and their values. Dimensions
     * are used when producing reports, which can summarise along one or more
     * dimensions.
     */
    type Dimensions = Map[Dimension,Value]

    /**
     * An empty dimensions collection.
     */
    val emptyDimensions : Dimensions = Map.empty

    /**
     * Pairs of dimension name and value.
     */
    type DimPair = (Dimension, Value)

    /**
     * Dimension pair sequence.
     */
    type DimPairs = Seq[DimPair]

    /**
     * Event types.
     */
    sealed abstract class EventKind
    case object Start extends EventKind
    case object Finish extends EventKind

    /**
     * Base class of profiling events.
     */
    class Event (val kind : EventKind, dimPairs : DimPair*) {

        /**
         * The dimensions of this event.
         */
        val dimensions : Dimensions =
            Map (dimPairs : _*)

        /**
         * The time in milliseconds when this event was created.
         */
        val time = nanoTime

    }

    /**
     * Event buffer.
     */
    val events = new ArrayBuffer[Event] ()

    /**
     * Reset the event buffer.
     */
    def reset () {
        events.clear ()
    }

    /**
     * Generate a `Start` event with the given dimensions.
     */
    @inline
    def start (dimPairs : DimPair*) {
        if (profiling)
            events += new Event (Start, dimPairs : _*)
    }

    /**
     * Generate a `Finish` event with the given dimensions.
     */
    @inline
    def finish (dimPairs : DimPair*) {
        if (profiling)
            events += new Event (Finish, dimPairs : _*)
    }

}
