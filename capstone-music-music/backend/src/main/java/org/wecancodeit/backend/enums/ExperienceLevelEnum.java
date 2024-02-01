package org.wecancodeit.backend.enums;

public enum ExperienceLevelEnum {
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced"),
    EXPERT("Expert"),
    PROFESSIONAL("Professional");
  
    private final String displayName;
  
    ExperienceLevelEnum(String displayName) {
      this.displayName = displayName;
    }
  
    public String getDisplayName() {
      return displayName;
    }
  }
