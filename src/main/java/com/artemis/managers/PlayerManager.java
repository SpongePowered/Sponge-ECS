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
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;

import java.util.HashMap;
import java.util.Map;


/**
 * You may sometimes want to specify to which player an entity belongs to.
 * <p/>
 * An entity can only belong to a single player at a time.
 *
 * @author Arni Arent
 */
public class PlayerManager extends Manager {
    private Map<Entity, String> playerByEntity;
    private Map<String, Bag<Entity>> entitiesByPlayer;

    public PlayerManager() {
        playerByEntity = new HashMap<Entity, String>();
        entitiesByPlayer = new HashMap<String, Bag<Entity>>();
    }

    public void setPlayer(Entity e, String player) {
        playerByEntity.put(e, player);
        Bag<Entity> entities = entitiesByPlayer.get(player);
        if (entities == null) {
            entities = new Bag<Entity>();
            entitiesByPlayer.put(player, entities);
        }
        entities.add(e);
    }

    public ImmutableBag<Entity> getEntitiesOfPlayer(String player) {
        Bag<Entity> entities = entitiesByPlayer.get(player);
        if (entities == null) {
            entities = new Bag<Entity>();
        }
        return entities;
    }

    public void removeFromPlayer(Entity e) {
        String player = playerByEntity.get(e);
        if (player != null) {
            Bag<Entity> entities = entitiesByPlayer.get(player);
            if (entities != null) {
                entities.remove(e);
            }
        }
    }

    public String getPlayer(Entity e) {
        return playerByEntity.get(e);
    }

    @Override
    protected void initialize() {
    }

    @Override
    public void deleted(Entity e) {
        removeFromPlayer(e);
    }
}
