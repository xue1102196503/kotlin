/*
 * Copyright 2010-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.util.slicedMap

import java.util.*
import java.util.function.BiConsumer

private const val MAGIC: Int = -1268542259
private const val MAX_SHIFT = 27
private const val THRESHOLD = ((1L shl 32) * 0.5).toInt() // 50% fill factor for speed

class MyMap<K : Any, V : Any> : AbstractMutableMap<K, V>() {
    private var shift = MAX_SHIFT
    private var length = 1 shl (32 - shift)
    private var arrSize = length shl 1
    private var array = arrayOfNulls<Any>(arrSize)

    private var size_ = 0

    override val size
        get() = size_

    override fun get(key: K): V? {
        var i = ((key.hashCode() * MAGIC) ushr shift) shl 1
        var k = array[i]

        while (true) {
            if (k === key) return array[i + 1] as V
            if (k == null) return null
            if (key == k) return array[i + 1] as V
            if (i == 0) {
                i = arrSize
            }
            i -= 2
            k = array[i]
        }
    }

    override fun put(key: K, value: V): V? {
        if (put(array, arrSize, shift, key, value)) {
            if (++size_ >= (THRESHOLD ushr shift)) {
                rehash()
            }
        }

        // Not very correct
        return null
    }

    private fun rehash() {
        val newShift = maxOf(shift - 3, 0)
        val newLength = 1 shl (32 - newShift)
        val newArraySize = newLength shl 1
        val newArray = arrayOfNulls<Any>(newArraySize)

        var i = 0
        while (i < arrSize) {
            val key = array[i]
            if (key != null) {
                put(newArray, newArraySize, newShift, key, array[i + 1])
            }
            i += 2
        }

        shift = newShift
        length = newLength
        arrSize = newArraySize
        array = newArray
    }

    override fun clear() {
        shift = MAX_SHIFT
        length = 1 shl (32 - shift)
        arrSize = length shl 1
        arrayOfNulls<Any>(arrSize)

        size_ = 0
    }

    override fun forEach(action: BiConsumer<in K, in V>) {
        var i = 0
        while (i < arrSize) {
            val key = array[i]
            if (key != null) {
                @Suppress("UNCHECKED_CAST")
                action.accept(key as K, array[i + 1] as V)
            }
            i += 2
        }
    }

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() {
            val result = HashSet<MutableMap.MutableEntry<K, V>>(size)

            forEach { k, v ->
                result.add(MyEntry(k, v))
            }

            return Collections.unmodifiableSet(result)
        }

    private class MyEntry<K, V>(
            override val key: K,
            override val value: V
    ) : MutableMap.MutableEntry<K, V> {
        override fun setValue(newValue: V): V {
            throw IllegalStateException("MyMap.MyEntry::setValue is not supported and hardly will be")
        }
    }
}

private fun put(array: Array<Any?>, arrSize: Int, aShift: Int, key: Any, value: Any?): Boolean {
    var i = ((key.hashCode() * MAGIC) ushr aShift) shl 1
    var k = array[i]

    while (true) {
        if (k === key) break
        if (k == null) {
            array[i] = key
            array[i + 1] = value
            return true
        }
        if (key == k) break
        if (i == 0) {
            i = arrSize
        }
        i -= 2
        k = array[i]
    }

    array[i + 1] = value

    return false
}
