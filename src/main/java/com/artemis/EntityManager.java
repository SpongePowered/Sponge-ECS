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
package com.artemis;

import java.util.BitSet;

import com.artemis.utils.Bag;

public class EntityManager extends Manager {
	private Bag<Entity> entities;
	private BitSet disabled;
	
	private int active;
	private long added;
	private long created;
	private long deleted;

	private IdentifierPool identifierPool;
	
	public EntityManager() {
		entities = new Bag<Entity>();
		disabled = new BitSet();
		identifierPool = new IdentifierPool();
	}
	
	@Override
	protected void initialize() {
	}

	protected Entity createEntityInstance() {
		Entity e = new Entity(world, identifierPool.checkOut());
		created++;
		return e;
	}
	
	@Override
	public void added(Entity e) {
		active++;
		added++;
		entities.set(e.getId(), e);
	}
	
	@Override
	public void enabled(Entity e) {
		disabled.clear(e.getId());
	}
	
	@Override
	public void disabled(Entity e) {
		disabled.set(e.getId());
	}
	
	@Override
	public void deleted(Entity e) {
		entities.set(e.getId(), null);
		
		disabled.clear(e.getId());
		
		identifierPool.checkIn(e.getId());
		
		active--;
		deleted++;
	}


	/**
	 * Check if this entity is active.
	 * Active means the entity is being actively processed.
	 * 
	 * @param entityId
	 * @return true if active, false if not.
	 */
	public boolean isActive(int entityId) {
		return entities.get(entityId) != null;
	}
	
	/**
	 * Check if the specified entityId is enabled.
	 * 
	 * @param entityId
	 * @return true if the entity is enabled, false if it is disabled.
	 */
	public boolean isEnabled(int entityId) {
		return !disabled.get(entityId);
	}
	
	/**
	 * Get a entity with this id.
	 * 
	 * @param entityId
	 * @return the entity
	 */
	protected Entity getEntity(int entityId) {
		return entities.get(entityId);
	}
	
	/**
	 * Get how many entities are active in this world.
	 * @return how many entities are currently active.
	 */
	public int getActiveEntityCount() {
		return active;
	}
	
	/**
	 * Get how many entities have been created in the world since start.
	 * Note: A created entity may not have been added to the world, thus
	 * created count is always equal or larger than added count.
	 * @return how many entities have been created since start.
	 */
	public long getTotalCreated() {
		return created;
	}
	
	/**
	 * Get how many entities have been added to the world since start.
	 * @return how many entities have been added.
	 */
	public long getTotalAdded() {
		return added;
	}
	
	/**
	 * Get how many entities have been deleted from the world since start.
	 * @return how many entities have been deleted since start.
	 */
	public long getTotalDeleted() {
		return deleted;
	}
	
	
	
	/*
	 * Used only internally to generate distinct ids for entities and reuse them.
	 */
	private class IdentifierPool {
		private Bag<Integer> ids;
		private int nextAvailableId;

		public IdentifierPool() {
			ids = new Bag<Integer>();
		}
		
		public int checkOut() {
			if(ids.size() > 0) {
				return ids.removeLast();
			}
			return nextAvailableId++;
		}
		
		public void checkIn(int id) {
			ids.add(id);
		}
	}

}
