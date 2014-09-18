/**
 * This file is part of Artemis, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2014 SpongePowered <http://spongepowered.org/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.artemis.managers;

import com.artemis.Entity;
import com.artemis.Manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * If you need to tag any entity, use this. A typical usage would be to tag
 * entities such as "PLAYER", "BOSS" or something that is very unique.
 *
 * @author Arni Arent
 */
public class TagManager extends Manager {
    private Map<String, Entity> entitiesByTag;
    private Map<Entity, String> tagsByEntity;

    public TagManager() {
        entitiesByTag = new HashMap<String, Entity>();
        tagsByEntity = new HashMap<Entity, String>();
    }

    public void register(String tag, Entity e) {
        entitiesByTag.put(tag, e);
        tagsByEntity.put(e, tag);
    }

    public void unregister(String tag) {
        tagsByEntity.remove(entitiesByTag.remove(tag));
    }

    public boolean isRegistered(String tag) {
        return entitiesByTag.containsKey(tag);
    }

    public Entity getEntity(String tag) {
        return entitiesByTag.get(tag);
    }

    public Collection<String> getRegisteredTags() {
        return tagsByEntity.values();
    }

    @Override
    public void deleted(Entity e) {
        String removedTag = tagsByEntity.remove(e);
        if (removedTag != null) {
            entitiesByTag.remove(removedTag);
        }
    }

    @Override
    protected void initialize() {
    }
}
