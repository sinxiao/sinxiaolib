package ae.sinxiao.android.selectimage;

public class ScreenBean {
    private int widthPixels;
    private int heightPixels;
    private float density;
    private int densityDpi;

    public int getDensityDpi() {
        return densityDpi;
    }

    public void setDensityDpi(int densityDpi) {
        this.densityDpi = densityDpi;
    }

    public int getWidthPixels() {
        return widthPixels;
    }

    public void setWidthPixels(int widthPixels) {
        this.widthPixels = widthPixels;
    }

    public int getHeightPixels() {
        return heightPixels;
    }

    public void setHeightPixels(int heightPixels) {
        this.heightPixels = heightPixels;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

}
