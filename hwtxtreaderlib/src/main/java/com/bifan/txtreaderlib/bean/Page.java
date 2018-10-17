package com.bifan.txtreaderlib.bean;

import com.bifan.txtreaderlib.interfaces.ICursor;
import com.bifan.txtreaderlib.interfaces.IPage;
import com.bifan.txtreaderlib.interfaces.ITxtLine;

import java.util.ArrayList;
import java.util.List;


/*
 * create by bifan-wei
 * 2017-11-13
 */
public class Page implements IPage, ICursor<ITxtLine> {
    private int CurrentIndex;
    private List<ITxtLine> lines = null;
    private boolean fullPage = false;

    @Override
    public int CurrentIndex() {
        return CurrentIndex;
    }

    @Override
    public void addLineTo(ITxtLine line, int index) {
        if (line == null) {
            throw new NullPointerException("line == null on addLine form Page");
        }
        if (lines == null) {
            lines = new ArrayList<>();
        }
        lines.add(index, line);
    }

    @Override
    public void addLine(ITxtLine line) {
        if (line == null) {
            throw new NullPointerException("line == null on addLine form Page");
        }
        if (lines == null) {
            lines = new ArrayList<>();
        }
        lines.add(line);
    }

    @Override
    public void setLines(List<ITxtLine> lines) {
        this.lines = lines;
    }

    @Override
    public int getCount() {
        return lines == null ? 0 : lines.size();
    }

    @Override
    public void moveToPosition(int index) {
        if (HasData()) {
            if (index < 0 || index >= getCount()) {
                throw new ArrayIndexOutOfBoundsException
                        (" moveToPosition index OutOfBoundsException from page");
            }
            CurrentIndex = index;
            Current();
        }

    }

    @Override
    public TxtChar getFirstChar() {
        ITxtLine firstLine = getFirstLine();
        if (!firstLine.HasData()) {
            return null;
        } else {
            return firstLine.getFirstChar();
        }
    }

    @Override
    public void moveToFirst() {
        moveToPosition(0);
    }

    @Override
    public TxtChar getLastChar() {
        ITxtLine lastLine = getLastLine();
        if (!lastLine.HasData()) {
            return null;
        } else {
            return lastLine.getLastChar();
        }
    }

    @Override
    public void moveToLast() {
        CurrentIndex = getCount() - 1;
        if (CurrentIndex < 0) {
            CurrentIndex = 0;
        }
        moveToPosition(CurrentIndex);
    }

    @Override
    public ITxtLine getFirstLine() {
        moveToFirst();
        return Current();
    }

    @Override
    public void moveToNext() {
        CurrentIndex++;
        if (CurrentIndex >= getCount()) {
            CurrentIndex = getCount() - 1;
        }
        if (CurrentIndex < 0) {
            CurrentIndex = 0;
        }
        moveToPosition(CurrentIndex);
    }

    @Override
    public ITxtLine getLastLine() {
        moveToLast();
        return Current();
    }

    @Override
    public void moveToPrevious() {
        CurrentIndex--;
        if (CurrentIndex < 0) {
            CurrentIndex = 0;
        }
        moveToPosition(CurrentIndex);
    }

    @Override
    public boolean isFirst() {
        return CurrentIndex == 0;
    }

    @Override
    public ICursor<ITxtLine> getLineCursor() {
        return this;
    }

    @Override
    public boolean isLast() {
        return CurrentIndex == getCount() - 1;
    }

    @Override
    public boolean isFullPage() {
        return fullPage;
    }

    @Override
    public void setFullPage(boolean fullPage) {
        this.fullPage = fullPage;
    }

    @Override
    public int getLineNum() {
        return getCount();
    }

    private Boolean AfterLast = false;
    private Boolean BeforeFirst = false;

    @Override
    public boolean isBeforeFirst() {
        return BeforeFirst;
    }


    @Override
    public boolean isAfterLast() {
        return AfterLast;
    }

    @Override
    public Boolean HasData() {
        return getCount() > 0;
    }

    @Override
    public ITxtLine Pre() {
        CurrentIndex--;
        if (CurrentIndex < 0) {
            CurrentIndex = 0;
        }
        moveToPosition(CurrentIndex);
        return Current();
    }

    @Override
    public ITxtLine Next() {
        CurrentIndex++;
        if (CurrentIndex >= getCount()) {
            CurrentIndex = getCount() - 1;
        }
        if (CurrentIndex < 0) {
            CurrentIndex = 0;
        }
        moveToPosition(CurrentIndex);
        return Current();
    }

    @Override
    public ITxtLine Current() {
        AfterLast = isLast();
        BeforeFirst = isFirst();
        return lines == null ? null : getLine(CurrentIndex);
    }

    @Override
    public ITxtLine getLine(int index) {
        return lines == null ? null : lines.get(index);
    }

    @Override
    public String toString() {
        String str = "";
        if (HasData()) {
            for (ITxtLine l : lines) {
                str = str + l.getLineStr() + "\r\n";
            }
        }
        return str;
    }

    @Override
    public List<ITxtLine> getLines() {
        return lines;
    }
}
