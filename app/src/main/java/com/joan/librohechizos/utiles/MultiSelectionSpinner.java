package com.joan.librohechizos.utiles;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Build;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.joan.librohechizos.R;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class MultiSelectionSpinner extends android.support.v7.widget.AppCompatSpinner implements OnMultiChoiceClickListener {
    String[] _items = null;
    boolean[] mSelection = null;
    ArrayAdapter<String> simple_adapter;

    public MultiSelectionSpinner(Context context) {
        super(context);
        simple_adapter = new ArrayAdapter<String>(context,
                R.layout.support_simple_spinner_dropdown_item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
          //  this.setLayoutMode(Spinner.MODE_DIALOG);
        }
        super.setAdapter(simple_adapter);

    }

    public MultiSelectionSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        simple_adapter = new ArrayAdapter<String>(context,
                R.layout.support_simple_spinner_dropdown_item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
           // this.setLayoutMode(Spinner.MODE_DIALOG);
        }
        super.setAdapter(simple_adapter);
    }

    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (mSelection != null && which < mSelection.length) {
            mSelection[which] = isChecked;
            simple_adapter.clear();
            //simple_adapter.add(buildSelectedItemString());
            simple_adapter.add(buildSelectedItemString2());
        } else {
            throw new IllegalArgumentException(
                    "Argument 'which' is out of bounds.");
        }
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(_items, mSelection, this);
        builder.setPositiveButton("OK", this);
        builder.show();
        return true;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        throw new RuntimeException(
                "setAdapter is not supported by MultiSelectSpinner.");
    }

    public void setItems(String[] items) {
        _items = items;
        mSelection = new boolean[_items.length];
        simple_adapter.clear();
        simple_adapter.add("Nivel ");
        Arrays.fill(mSelection, false);

    }

    public void setItems(List<String> items) {
        _items = items.toArray(new String[items.size()]);
        mSelection = new boolean[_items.length];
        simple_adapter.clear();
        simple_adapter.add("Nivel ");
        Arrays.fill(mSelection, false);
    }

    public void setSelection(String[] selection) {
        for (String cell : selection) {
            for (int j = 0; j < _items.length; ++j) {
                if (_items[j].equals(cell)) {
                    mSelection[j] = true;
                }
            }
        }
        simple_adapter.add(buildSelectedItemString2());

    }

    public void setSelection(List<String> selection) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        for (String sel : selection) {
            for (int j = 0; j < _items.length; ++j) {
                if (_items[j].equals(sel)) {
                    mSelection[j] = true;
                }
            }
        }
        simple_adapter.clear();
        //simple_adapter.add(buildSelectedItemString());
        simple_adapter.add(buildSelectedItemString2());
    }

    public void setSelection(int index) {
        if (index != -1) {
            for (int i = 0; i < mSelection.length; i++) {
                mSelection[i] = false;
            }
            if (index >= 0 && index < mSelection.length) {
                mSelection[index] = true;
            } else {
                throw new IllegalArgumentException("Index " + index
                        + " is out of bounds.");
            }
        }
        simple_adapter.clear();
        //mi propia vercion
        simple_adapter.add(buildSelectedItemString2());
        //simple_adapter.add(buildSelectedItemString());
    }

    public void setSelection(int[] selectedIndicies) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        for (int index : selectedIndicies) {
            if (index >= 0 && index < mSelection.length) {
                mSelection[index] = true;
            } else {
                throw new IllegalArgumentException("Index " + index
                        + " is out of bounds.");
            }
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString2());
    }

    public void setAllSelection(boolean seteo){
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = seteo;
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString2());
    }

    public List<String> getSelectedStrings() {
        List<String> selection = new LinkedList<String>();
        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                selection.add(_items[i]);
            }
        }
        return selection;
    }

    public List<Integer> getSelectedIndicies() {
        List<Integer> selection = new LinkedList<Integer>();
        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                selection.add(i);
            }
        }
        return selection;
    }

    /*  private String buildSelectedItemString() {
          StringBuilder sb = new StringBuilder();
          boolean foundOne = false;

          for (int i = 0; i < _items.length; ++i) {
              if (mSelection[i]) {
                  if (foundOne) {
                      sb.append(", ");
                  }
                  foundOne = true;

                  sb.append(_items[i]);
              }
          }
          return sb.toString();
      }*/
    //metodo no generico modificado para mi propio uso
    private String buildSelectedItemString2() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;
        sb.append("Nivel ");
        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;

                sb.append(_items[i].replace("Nivel ", ""));
            }
        }
        return sb.toString();
    }


    public String getSelectedItemsAsString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;
                sb.append(_items[i]);
            }
        }
        return sb.toString();
    }
}

