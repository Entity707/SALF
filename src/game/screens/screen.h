#pragma once

enum class ScreenStatus {
    ACTIVE,
    LOADING_IN,
    EXIT
};
enum class Screens {
    NONE,
    TITLE_SCREEN
};
class Screen {
private:
    ScreenStatus screenStatus = ScreenStatus::LOADING_IN;
    Screens nextScreen = Screens::NONE;
public:
    virtual ~Screen() = default;

    ScreenStatus getScreenStatus() { return screenStatus; }
    virtual void drawScreen() = 0;
    virtual void tick() = 0;
};
