package com.google;

import java.util.*;
import java.util.stream.Collectors;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private boolean isPlaying;
  private Video currentPlaying;
  private Video randomVideo;
  private final Map<String, VideoPlaylist> videoPlaylistMap;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
    isPlaying = false;
    currentPlaying = null;
    this.videoPlaylistMap = new HashMap<>();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    System.out.println("Here's a list of all available videos:");
    List<Video> videos = this.videoLibrary.getVideos();
    Collections.sort(videos, new VideoCompare());
    for (Video video:videos) {
      if (video.isFlagged()) {
        System.out.println(video + " - FLAGGED (reason: " + video.getReason() + ")");
      } else {
        System.out.print("  " + video);
        System.out.println();
      }
    }
  }

  public void playVideo(String videoId) {
    Video video = this.videoLibrary.getVideo(videoId);
    if(video == null){
      System.out.println("Cannot play video: Video does not exist");
    }
    else {
      Video currentVideo = this.currentPlaying;
      if (currentVideo != null) {
        System.out.println("Stopping video: " + currentVideo.getTitle());
        currentPlaying = null;
        isPlaying = false;
      }
      if (video.isFlagged()) {
        System.out.println("Cannot play video: Video is currently flagged (reason: " + video.getReason() + ")");
      }
      else {
        currentPlaying = video;
        isPlaying = true;
        System.out.println("Playing video: " + currentPlaying.getTitle());
      }
    }
  }

  public void stopVideo() {
    Video currentVideo = this.currentPlaying;
    if(currentVideo != null){
      System.out.println("Stopping video: " + currentPlaying.getTitle());
      isPlaying = false;
      currentPlaying = null;
    }
    else{
      if (currentVideo == null){
        System.out.println("Cannot stop video: No video is currently playing");
      }
    }
  }

  public void playRandomVideo() {
    Random r = new Random();
    List<Video> videoList = this.videoLibrary.getVideos();
    int randomIndex = r.nextInt(videoList.size());
    Video newVideo = videoList.get(randomIndex);

    Video videos = this.currentPlaying;
    if (videos != null) {
      System.out.println("Stopping video: " + currentPlaying.getTitle());
    } else if (newVideo.isFlagged()) {
        System.out.println("No videos available");
        currentPlaying = null;
        isPlaying = false;
      }
    currentPlaying = newVideo;
    isPlaying = true;
    System.out.println("Playing video: " + newVideo.getTitle());
  }

  public void pauseVideo() {
    Video currentVideo = this.currentPlaying;
    if(currentVideo == null){
      System.out.println("Cannot pause video: No video is currently playing");
    }
    else{
      if(this.isPlaying){
        System.out.println("Pausing video: " + currentVideo.getTitle());
      }
      else{
        System.out.println("Video already paused: " + currentVideo.getTitle());
      }
      this.isPlaying = false;
    }
  }

  public void continueVideo() {
    Video currentVideo = this.currentPlaying;
    if(currentPlaying == null){
      System.out.println("Cannot continue video: No video is currently playing");
    }
    else{
      if (this.isPlaying) {
        System.out.println("Cannot continue video: Video is not paused");
      } else {
        System.out.println("Continuing video: " + currentVideo.getTitle());
        this.isPlaying = true;
      }
    }
  }

  public void showPlaying() {
    Video currentVideo = this.currentPlaying;
    if (currentPlaying == null || currentVideo.isFlagged()) {
      System.out.println("No video is currently playing");
    }
    else{
      if(this.isPlaying){
        System.out.println("Currently playing: " + currentVideo.toString());
      }
      else{
        System.out.println("Currently playing: " + currentVideo.toString() + " - PAUSED");
        this.isPlaying = false;
      }
    }
  }

  public void createPlaylist(String playlistName) {
    if(videoPlaylistMap.containsKey(playlistName.toLowerCase())){
      System.out.println("Cannot create playlist: A playlist with the same name already exists");
    }
    else {
      VideoPlaylist create = new VideoPlaylist(playlistName);
      System.out.println("Successfully created new playlist: " + playlistName);
      videoPlaylistMap.put(playlistName.toLowerCase(), create);
    }
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    Video newVideo = this.videoLibrary.getVideo(videoId);

    if(!videoPlaylistMap.containsKey(playlistName.toLowerCase())){
      System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
    }
    else if(newVideo == null &&  videoPlaylistMap.containsKey(playlistName.toLowerCase())){
      System.out.println("Cannot add video to " + playlistName + ": Video does not exist");
    }
    else if(newVideo.isFlagged()){
      System.out.println("Cannot add video to " + playlistName + ": Video is currently flagged (reason: " + newVideo.getReason() + ")");
    }
    else if(videoPlaylistMap.get(playlistName.toLowerCase()).playlists.containsValue(newVideo)) {
        System.out.println("Cannot add video to " + playlistName + ": Video already added");
      }
    else{
      videoPlaylistMap.get(playlistName.toLowerCase()).playlists.put(videoId, newVideo);
      System.out.println("Added video to " + playlistName + ": " + newVideo.getTitle());
    }
  }

  public void showAllPlaylists() {
    if(videoPlaylistMap.size() == 0){
      System.out.println("No playlists exist yet");
    }
    else{
      System.out.println("Showing all playlists: ");
      for(String playlistListing : videoPlaylistMap.keySet()){
        System.out.println(playlistListing);
      }
    }
  }

  public void showPlaylist(String playlistName) {
    if (!videoPlaylistMap.containsKey(playlistName.toLowerCase())) {
      System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
    }
    else if (videoPlaylistMap.get(playlistName.toLowerCase()).playlists.isEmpty()) {
      System.out.println("Showing playlist: " + playlistName);
      System.out.println("No videos here yet");
    }
    else {
      System.out.println("Showing playlist: " + playlistName);
      for(String videoListing : videoPlaylistMap.get(playlistName.toLowerCase()).playlists.keySet()) {
        Video videos = videoLibrary.getVideo(videoListing);
        if (videos.isFlagged()) {
          System.out.println(videos.toString() + " - FLAGGED (reason: " + videos.getReason() + ")");
        } else {
          System.out.println(videos.toString());
        }
      }
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    Video video = this.videoLibrary.getVideo(videoId);

    if(!videoPlaylistMap.containsKey(playlistName.toLowerCase())){
      System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
    }
    else if (video == null){
      System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
    }
    else if(videoPlaylistMap.get(playlistName.toLowerCase()).playlists.get(videoId) == null){
      System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
    }
    else{
      System.out.println("Removed video from " + playlistName + ": " + video.getTitle());
      videoPlaylistMap.get(playlistName.toLowerCase()).playlists.remove(videoId, video);
    }
  }

  public void clearPlaylist(String playlistName) {
    if(!videoPlaylistMap.containsKey(playlistName.toLowerCase())){
      System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
    }
    else{
    videoPlaylistMap.get(playlistName.toLowerCase()).playlists.clear();
    System.out.println("Successfully removed all videos from " + playlistName);
    }
  }

  public void deletePlaylist(String playlistName) {
    if(!videoPlaylistMap.containsKey(playlistName.toLowerCase())) {
      System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
    }
    else{
    videoPlaylistMap.remove(playlistName.toLowerCase());
    System.out.println("Deleted playlist: " + playlistName);
    }
  }

  public void displaySearchResults(String searchTerm, List<Video> results) {
    if(results.size()==0) {
      System.out.println("No search results for " + searchTerm);
    } else {
      System.out.println("Here are the results for " + searchTerm+":");
      int count = 1;
      for(Video v : results) {
        if(v.isFlagged()){
          System.out.print("");
        }
        else{
          System.out.println(count + ") " + v);
          count++;
        }
      }
      System.out.println("Would you like to play any of the above? If yes, specify the number of the video. \n"
              + "If your answer is not a valid number, we will assume it's a no.");
      var scanner = new Scanner(System.in);
      if(scanner.hasNextInt()) {
        var option = scanner.nextInt();
        if(results.size()>=option) {
          Video videoToPlay = results.get(option-1);
          playVideo(videoToPlay.getVideoId());
        }
      }
    }
  }

  public void searchVideos(String searchTerm) {
    List<Video> results = videoLibrary.getVideos().stream()
            .filter(v -> v.getTitle().toLowerCase().contains(searchTerm.toLowerCase()))
            .sorted(Comparator.comparing(Video::getTitle))
            .collect(Collectors.toList());

    displaySearchResults(searchTerm, results);
  }

  public void searchVideosWithTag(String videoTag) {
    List<Video> tagResults = videoLibrary.getVideos().stream().filter(v -> v.getTags().toString().toLowerCase().contains(videoTag.toLowerCase()))
            .sorted(Comparator.comparing(Video::getTitle))
            .collect(Collectors.toList());

    displaySearchResults(videoTag, tagResults);
  }

  public void flagVideo(String videoId) {
    flagVideo(videoId, "Not supplied");
  }

  public void flagVideo(String videoId, String reason) {
    Video flagVideo = videoLibrary.getVideo(videoId);
    if (flagVideo == null) {
      System.out.println("Cannot flag video: Video does not exist");
    }
    else {
      if (flagVideo.isFlagged()) {
        System.out.println("Cannot flag video: Video is already flagged");
        return;
      }
      else {
        if(currentPlaying == this.videoLibrary.getVideo(videoId)){
        stopVideo();
        currentPlaying = null;
      }
        System.out.println("Successfully flagged video: " + flagVideo.getTitle() + " (reason: " + reason + ")");
        flagVideo.markFlagged(reason);
      }
    }
  }

  public void allowVideo(String videoId) {
    Video unflagVideo = this.videoLibrary.getVideo(videoId);
    if (unflagVideo == null) {
      System.out.println("Cannot remove flag from video: Video does not exist");
    }
    else {
      if (!unflagVideo.isFlagged()) {
        System.out.println("Cannot remove flag from video: Video is not flagged");
      }
      else {
        System.out.println("Successfully removed flag from video: " + unflagVideo.getTitle());
      }
    }
    }
}