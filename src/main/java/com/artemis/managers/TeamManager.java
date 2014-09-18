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

import com.artemis.Manager;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;

import java.util.HashMap;
import java.util.Map;


/**
 * Use this class together with PlayerManager.
 * <p/>
 * You may sometimes want to create teams in your game, so that
 * some players are team mates.
 * <p/>
 * A player can only belong to a single team.
 *
 * @author Arni Arent
 */
public class TeamManager extends Manager {
    private Map<String, Bag<String>> playersByTeam;
    private Map<String, String> teamByPlayer;

    public TeamManager() {
        playersByTeam = new HashMap<String, Bag<String>>();
        teamByPlayer = new HashMap<String, String>();
    }

    @Override
    protected void initialize() {
    }

    public String getTeam(String player) {
        return teamByPlayer.get(player);
    }

    public void setTeam(String player, String team) {
        removeFromTeam(player);

        teamByPlayer.put(player, team);

        Bag<String> players = playersByTeam.get(team);
        if (players == null) {
            players = new Bag<String>();
            playersByTeam.put(team, players);
        }
        players.add(player);
    }

    public ImmutableBag<String> getPlayers(String team) {
        return playersByTeam.get(team);
    }

    public void removeFromTeam(String player) {
        String team = teamByPlayer.remove(player);
        if (team != null) {
            Bag<String> players = playersByTeam.get(team);
            if (players != null) {
                players.remove(player);
            }
        }
    }
}
