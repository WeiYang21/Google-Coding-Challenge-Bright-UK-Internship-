package com.google;

import java.util.*;
/** A class used to represent a Playlist */
class VideoPlaylist {
    String playlistTitle;
    LinkedHashMap<String,Video>playlists;

    VideoPlaylist(String playlistTitle){
        this.playlistTitle = playlistTitle;
        this.playlists = new LinkedHashMap<String,Video>();
    }

    String getPlaylistTitle(){return playlistTitle;}
}
