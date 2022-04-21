package com.github.vladosspasi.mes.Settings.Templates;

import android.content.ContentValues;

import java.util.ArrayList;

import static com.github.vladosspasi.mes.DataBaseHelper.FIELD_SCALES_ID;

//Класс шаблона для более удобной передачи
public class Template {
    private ArrayList<Integer> scalesIds;
    private ArrayList<ContentValues> scalesList;
    private String name;
    private String comment;
    private int id;

    public Template() {
        name = "";
        comment = "";
        scalesIds = new ArrayList<>();
        scalesList = new ArrayList<>();
    }

    public ArrayList<Integer> getScalesIds() {
        return scalesIds;
    }

    public void setScalesIds(ArrayList<Integer> scalesIds) {
        this.scalesIds = scalesIds;
    }

    public ArrayList<ContentValues> getScalesList() {
        return scalesList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void clear() {
        name = "";
        comment = "";
        scalesIds = new ArrayList<>();
        scalesList = new ArrayList<>();
    }

    public void addToScalesList(ContentValues scale) {
        scalesIds.add(scale.getAsInteger(FIELD_SCALES_ID));
        scalesList.add(scale);
    }
}