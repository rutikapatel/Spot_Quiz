package com.example.spotquiz.adapters;

import com.example.spotquiz.listeners.QuestionChangeListener;

public class Grid {
    Integer number;

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    Boolean finished;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    private QuestionChangeListener questionChangeListener;

    public void setOnQuestionChange(QuestionChangeListener eventListener)
    {
        questionChangeListener = eventListener;
    }

    public void change(int color)
    {
        if(questionChangeListener != null)
        {
            questionChangeListener.onQuestionChange(color);
        }

    }
}
