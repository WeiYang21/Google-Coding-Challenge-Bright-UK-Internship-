package com.google;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** A class used to represent a video. */
class Video {

  private final String title;
  private final String videoId;
  private final List<String> tags;
  private boolean flagged = false;
  private String flagReason = null;

  Video(String title, String videoId, List<String> tags) {
    this.title = title;
    this.videoId = videoId;
    this.tags = Collections.unmodifiableList(tags);
  }

  /** Returns the title of the video. */
  String getTitle() {
    return title;
  }

  /** Returns the video id of the video. */
  String getVideoId() {
    return videoId;
  }

  /** Returns a readonly collection of the tags of the video. */
  List<String> getTags() {
    return tags;
  }

  public boolean isFlagged(){
    return flagged;
  }

  public void markFlagged(String reason){
    if(reason != null) {
      flagged = true;
      flagReason = reason;
    }
  }
  public String getReason(){
    return flagReason;
  }

  public String toString(){
    return  title + " (" +videoId+ ") [" +String.join(" ", tags) + "]";
  }
}
