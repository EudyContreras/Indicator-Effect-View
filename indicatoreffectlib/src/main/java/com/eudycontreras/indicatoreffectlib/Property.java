package com.eudycontreras.indicatoreffectlib;

import androidx.annotation.RestrictTo;

/**
 * <b>Note:</b> Unlicensed private property of the author and creator
 * unauthorized use of this class outside of the Indicator Effect project
 * by the author may result on legal prosecution.
 * <p>
 * Created by <B>Eudy Contreras</B>
 *
 * @author  Eudy Contreras
 * @version 1.0
 * @since   2018-03-31
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class Property<T> {
    private T value;

    public Property(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
