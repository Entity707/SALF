package com.game.ui;

import com.game.TextAlignment;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;


import javax.swing.Action;

public class TabBar extends UserInterface{
  //required
  private ObjectArrayList<String> tabs;
  private Action next;
  private Action back;
  private Action select;
  private long x;
  private long y;
  private long width;
  private long height;
  private boolean explicitActivation;
  private boolean textFollowsWallColor;
  private long textSize;
  private boolean isHorizontal;
  //optional
  private int textColor;
  private TextAlignment textAlignment;
  private int backgroundColor;
  private int wallColor;
  private int selectColor;
  private int activeColor;
  private int wallThickness;
  private int activeIndex;
  private int selectedIndex;
  
  //generated
  private long cellWidth;
  
  public TabBar(Builder builder){
    tabs = builder.tabs;
    next = builder.next;
    back = builder.back;
    select = builder.select;
    x = builder.x;
    y = builder.y;
    width = builder.width;
    height = builder.height;
    explicitActivation = builder.explicitActivation;
    textFollowsWallColor = builder.textFollowsWallColor;
    textSize = builder.textSize;
    isHorizontal = builder.isHorizontal;
    textColor = builder.textColor;
    textAlignment = builder.textAlignment;
    backgroundColor = builder.backgroundColor;
    wallColor = builder.wallColor;
    selectColor = builder.selectColor;
    activeColor = builder.activeColor;
    wallThickness = builder.wallThickness;
    activeIndex = builder.activeIndex;
    selectedIndex = builder.selectedIndex;
    
    cellWidth = builder.width/ StrictMath.max(tabs.size(), 1);
  }
  
  @Override
  public void draw() {
  
  }
  
  @Override
  public void tick() {
  
  }
  
  public static class Builder{
    private final ObjectArrayList<String> tabs;
    private final Action next;
    private final Action back;
    private final Action select;
    private final long x;
    private final long y;
    private final long width;
    private final long height;
    private final boolean explicitActivation;
    private final boolean textFollowsWallColor;
    private final long textSize;
    private final boolean isHorizontal;
    
    private int textColor = 0xFFFFFFFF;
    private TextAlignment textAlignment = TextAlignment.CENTER_MIDDLE;
    private int backgroundColor = 0x4B4B4BFF;
    private int wallColor = 0x0096FFFF;
    private int selectColor = 0x00CAFFFF;
    private int activeColor = 0x00FFFFFF;
    private int wallThickness = 2;
    private int activeIndex = 0;
    private int selectedIndex = 0;
    
    public Builder(long x, long y, long width, long height, boolean explicitActivation, boolean textFollowsWallColor, long textSize, boolean isHorizontal, Action next, Action back, Action select, String... tabs){
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.explicitActivation = explicitActivation;
      this.textFollowsWallColor = textFollowsWallColor;
      this.textSize = textSize;
      this.isHorizontal = isHorizontal;
      this.next = next;
      this.back = back;
      this.select = select;
      this.tabs = new ObjectArrayList<>(tabs);
    }
    
    
    public TabBar build(){
      return new TabBar(this);
    }
    
    public Builder setActiveColor(int activeColor) {
      this.activeColor = activeColor;
      return this;
    }
    
    public Builder setActiveIndex(int activeIndex) {
      this.activeIndex = activeIndex;
      return this;
    }
    
    public Builder setBackgroundColor(int backgroundColor) {
      this.backgroundColor = backgroundColor;
      return this;
    }
    
    public Builder setSelectColor(int selectColor) {
      this.selectColor = selectColor;
      return this;
    }
    
    public Builder setSelectedIndex(int selectedIndex) {
      this.selectedIndex = selectedIndex;
      return this;
    }
    
    public Builder setTextAlignment(TextAlignment textAlignment) {
      this.textAlignment = textAlignment;
      return this;
    }
    
    public Builder setTextColor(int textColor) {
      this.textColor = textColor;
      return this;
    }
    
    public Builder setWallColor(int wallColor) {
      this.wallColor = wallColor;
      return this;
    }
    
    public Builder setWallThickness(int wallThickness) {
      this.wallThickness = wallThickness;
      return this;
    }
  }
  
  
  public int getActiveColor() {
    return activeColor;
  }
  
  public void setActiveColor(int activeColor) {
    this.activeColor = activeColor;
  }
  
  public int getActiveIndex() {
    return activeIndex;
  }
  
  public void setActiveIndex(int activeIndex) {
    this.activeIndex = activeIndex;
  }
  
  public Action getBack() {
    return back;
  }
  
  public void setBack(Action back) {
    this.back = back;
  }
  
  public int getBackgroundColor() {
    return backgroundColor;
  }
  
  public void setBackgroundColor(int backgroundColor) {
    this.backgroundColor = backgroundColor;
  }
  
  public long getCellWidth() {
    return cellWidth;
  }
  
  public void setCellWidth(long cellWidth) {
    this.cellWidth = cellWidth;
  }
  
  public boolean isExplicitActivation() {
    return explicitActivation;
  }
  
  public void setExplicitActivation(boolean explicitActivation) {
    this.explicitActivation = explicitActivation;
  }
  
  public long getHeight() {
    return height;
  }
  
  public void setHeight(long height) {
    this.height = height;
  }
  
  public boolean isHorizontal() {
    return isHorizontal;
  }
  
  public void setHorizontal(boolean horizontal) {
    isHorizontal = horizontal;
  }
  
  public Action getNext() {
    return next;
  }
  
  public void setNext(Action next) {
    this.next = next;
  }
  
  public Action getSelect() {
    return select;
  }
  
  public void setSelect(Action select) {
    this.select = select;
  }
  
  public int getSelectColor() {
    return selectColor;
  }
  
  public void setSelectColor(int selectColor) {
    this.selectColor = selectColor;
  }
  
  public int getSelectedIndex() {
    return selectedIndex;
  }
  
  public void setSelectedIndex(int selectedIndex) {
    this.selectedIndex = selectedIndex;
  }
  
  public ObjectArrayList<String> getTabs() {
    return tabs;
  }
  
  public void setTabs(ObjectArrayList<String> tabs) {
    this.tabs = tabs;
  }
  
  public TextAlignment getTextAlignment() {
    return textAlignment;
  }
  
  public void setTextAlignment(TextAlignment textAlignment) {
    this.textAlignment = textAlignment;
  }
  
  public int getTextColor() {
    return textColor;
  }
  
  public void setTextColor(int textColor) {
    this.textColor = textColor;
  }
  
  public boolean isTextFollowsWallColor() {
    return textFollowsWallColor;
  }
  
  public void setTextFollowsWallColor(boolean textFollowsWallColor) {
    this.textFollowsWallColor = textFollowsWallColor;
  }
  
  public long getTextSize() {
    return textSize;
  }
  
  public void setTextSize(long textSize) {
    this.textSize = textSize;
  }
  
  public int getWallColor() {
    return wallColor;
  }
  
  public void setWallColor(int wallColor) {
    this.wallColor = wallColor;
  }
  
  public int getWallThickness() {
    return wallThickness;
  }
  
  public void setWallThickness(int wallThickness) {
    this.wallThickness = wallThickness;
  }
  
  public long getWidth() {
    return width;
  }
  
  public void setWidth(long width) {
    this.width = width;
  }
  
  public long getX() {
    return x;
  }
  
  public void setX(long x) {
    this.x = x;
  }
  
  public long getY() {
    return y;
  }
  
  public void setY(long y) {
    this.y = y;
  }
}
