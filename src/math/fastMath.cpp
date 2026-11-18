#include "fastMath.h"

#include <array>
#include <cstdint>

#include "fixed.h"

namespace {
    constexpr int32_t SIN_TABLE_BITS = 10;
    constexpr int32_t SIN_TABLE_SIZE = 1 << SIN_TABLE_BITS;
    constexpr int32_t SIN_INDEX_SHIFT = 16 - SIN_TABLE_BITS;

    constexpr int32_t ATAN_TABLE_BITS = 9;
    constexpr int32_t ATAN_TABLE_SIZE = (1 << ATAN_TABLE_BITS) + 1;


    constexpr std::array<salf::fix_t, SIN_TABLE_SIZE> SIN_CACHE = {
        salf::fix_t{0}, salf::fix_t{2}, salf::fix_t{3}, salf::fix_t{5}, salf::fix_t{6}, salf::fix_t{8}, salf::fix_t{9},
        salf::fix_t{11}, salf::fix_t{13}, salf::fix_t{14}, salf::fix_t{16}, salf::fix_t{17}, salf::fix_t{19},
        salf::fix_t{20}, salf::fix_t{22}, salf::fix_t{24}, salf::fix_t{25}, salf::fix_t{27}, salf::fix_t{28},
        salf::fix_t{30}, salf::fix_t{31}, salf::fix_t{33}, salf::fix_t{34}, salf::fix_t{36}, salf::fix_t{38},
        salf::fix_t{39}, salf::fix_t{41}, salf::fix_t{42}, salf::fix_t{44}, salf::fix_t{45}, salf::fix_t{47},
        salf::fix_t{48}, salf::fix_t{50}, salf::fix_t{51}, salf::fix_t{53}, salf::fix_t{55}, salf::fix_t{56},
        salf::fix_t{58}, salf::fix_t{59}, salf::fix_t{61}, salf::fix_t{62}, salf::fix_t{64}, salf::fix_t{65},
        salf::fix_t{67}, salf::fix_t{68}, salf::fix_t{70}, salf::fix_t{71}, salf::fix_t{73}, salf::fix_t{74},
        salf::fix_t{76}, salf::fix_t{77}, salf::fix_t{79}, salf::fix_t{80}, salf::fix_t{82}, salf::fix_t{83},
        salf::fix_t{85}, salf::fix_t{86}, salf::fix_t{88}, salf::fix_t{89}, salf::fix_t{91}, salf::fix_t{92},
        salf::fix_t{94}, salf::fix_t{95}, salf::fix_t{97}, salf::fix_t{98}, salf::fix_t{99}, salf::fix_t{101},
        salf::fix_t{102}, salf::fix_t{104}, salf::fix_t{105}, salf::fix_t{107}, salf::fix_t{108}, salf::fix_t{109},
        salf::fix_t{111}, salf::fix_t{112}, salf::fix_t{114}, salf::fix_t{115}, salf::fix_t{117}, salf::fix_t{118},
        salf::fix_t{119}, salf::fix_t{121}, salf::fix_t{122}, salf::fix_t{123}, salf::fix_t{125}, salf::fix_t{126},
        salf::fix_t{128}, salf::fix_t{129}, salf::fix_t{130}, salf::fix_t{132}, salf::fix_t{133}, salf::fix_t{134},
        salf::fix_t{136}, salf::fix_t{137}, salf::fix_t{138}, salf::fix_t{140}, salf::fix_t{141}, salf::fix_t{142},
        salf::fix_t{144}, salf::fix_t{145}, salf::fix_t{146}, salf::fix_t{147}, salf::fix_t{149}, salf::fix_t{150},
        salf::fix_t{151}, salf::fix_t{152}, salf::fix_t{154}, salf::fix_t{155}, salf::fix_t{156}, salf::fix_t{157},
        salf::fix_t{159}, salf::fix_t{160}, salf::fix_t{161}, salf::fix_t{162}, salf::fix_t{164}, salf::fix_t{165},
        salf::fix_t{166}, salf::fix_t{167}, salf::fix_t{168}, salf::fix_t{170}, salf::fix_t{171}, salf::fix_t{172},
        salf::fix_t{173}, salf::fix_t{174}, salf::fix_t{175}, salf::fix_t{177}, salf::fix_t{178}, salf::fix_t{179},
        salf::fix_t{180}, salf::fix_t{181}, salf::fix_t{182}, salf::fix_t{183}, salf::fix_t{184}, salf::fix_t{185},
        salf::fix_t{186}, salf::fix_t{188}, salf::fix_t{189}, salf::fix_t{190}, salf::fix_t{191}, salf::fix_t{192},
        salf::fix_t{193}, salf::fix_t{194}, salf::fix_t{195}, salf::fix_t{196}, salf::fix_t{197}, salf::fix_t{198},
        salf::fix_t{199}, salf::fix_t{200}, salf::fix_t{201}, salf::fix_t{202}, salf::fix_t{203}, salf::fix_t{204},
        salf::fix_t{205}, salf::fix_t{206}, salf::fix_t{207}, salf::fix_t{207}, salf::fix_t{208}, salf::fix_t{209},
        salf::fix_t{210}, salf::fix_t{211}, salf::fix_t{212}, salf::fix_t{213}, salf::fix_t{214}, salf::fix_t{215},
        salf::fix_t{215}, salf::fix_t{216}, salf::fix_t{217}, salf::fix_t{218}, salf::fix_t{219}, salf::fix_t{220},
        salf::fix_t{220}, salf::fix_t{221}, salf::fix_t{222}, salf::fix_t{223}, salf::fix_t{224}, salf::fix_t{224},
        salf::fix_t{225}, salf::fix_t{226}, salf::fix_t{227}, salf::fix_t{227}, salf::fix_t{228}, salf::fix_t{229},
        salf::fix_t{229}, salf::fix_t{230}, salf::fix_t{231}, salf::fix_t{231}, salf::fix_t{232}, salf::fix_t{233},
        salf::fix_t{233}, salf::fix_t{234}, salf::fix_t{235}, salf::fix_t{235}, salf::fix_t{236}, salf::fix_t{237},
        salf::fix_t{237}, salf::fix_t{238}, salf::fix_t{238}, salf::fix_t{239}, salf::fix_t{239}, salf::fix_t{240},
        salf::fix_t{241}, salf::fix_t{241}, salf::fix_t{242}, salf::fix_t{242}, salf::fix_t{243}, salf::fix_t{243},
        salf::fix_t{244}, salf::fix_t{244}, salf::fix_t{245}, salf::fix_t{245}, salf::fix_t{245}, salf::fix_t{246},
        salf::fix_t{246}, salf::fix_t{247}, salf::fix_t{247}, salf::fix_t{248}, salf::fix_t{248}, salf::fix_t{248},
        salf::fix_t{249}, salf::fix_t{249}, salf::fix_t{249}, salf::fix_t{250}, salf::fix_t{250}, salf::fix_t{250},
        salf::fix_t{251}, salf::fix_t{251}, salf::fix_t{251}, salf::fix_t{252}, salf::fix_t{252}, salf::fix_t{252},
        salf::fix_t{252}, salf::fix_t{253}, salf::fix_t{253}, salf::fix_t{253}, salf::fix_t{253}, salf::fix_t{254},
        salf::fix_t{254}, salf::fix_t{254}, salf::fix_t{254}, salf::fix_t{254}, salf::fix_t{255}, salf::fix_t{255},
        salf::fix_t{255}, salf::fix_t{255}, salf::fix_t{255}, salf::fix_t{255}, salf::fix_t{255}, salf::fix_t{256},
        salf::fix_t{256}, salf::fix_t{256}, salf::fix_t{256}, salf::fix_t{256}, salf::fix_t{256}, salf::fix_t{256},
        salf::fix_t{256}, salf::fix_t{256}, salf::fix_t{256}, salf::fix_t{256}, salf::fix_t{256}, salf::fix_t{256},
        salf::fix_t{256}, salf::fix_t{256}, salf::fix_t{256}, salf::fix_t{256}, salf::fix_t{256}, salf::fix_t{256},
        salf::fix_t{256}, salf::fix_t{256}, salf::fix_t{255}, salf::fix_t{255}, salf::fix_t{255}, salf::fix_t{255},
        salf::fix_t{255}, salf::fix_t{255}, salf::fix_t{255}, salf::fix_t{254}, salf::fix_t{254}, salf::fix_t{254},
        salf::fix_t{254}, salf::fix_t{254}, salf::fix_t{253}, salf::fix_t{253}, salf::fix_t{253}, salf::fix_t{253},
        salf::fix_t{252}, salf::fix_t{252}, salf::fix_t{252}, salf::fix_t{252}, salf::fix_t{251}, salf::fix_t{251},
        salf::fix_t{251}, salf::fix_t{250}, salf::fix_t{250}, salf::fix_t{250}, salf::fix_t{249}, salf::fix_t{249},
        salf::fix_t{249}, salf::fix_t{248}, salf::fix_t{248}, salf::fix_t{248}, salf::fix_t{247}, salf::fix_t{247},
        salf::fix_t{246}, salf::fix_t{246}, salf::fix_t{245}, salf::fix_t{245}, salf::fix_t{245}, salf::fix_t{244},
        salf::fix_t{244}, salf::fix_t{243}, salf::fix_t{243}, salf::fix_t{242}, salf::fix_t{242}, salf::fix_t{241},
        salf::fix_t{241}, salf::fix_t{240}, salf::fix_t{239}, salf::fix_t{239}, salf::fix_t{238}, salf::fix_t{238},
        salf::fix_t{237}, salf::fix_t{237}, salf::fix_t{236}, salf::fix_t{235}, salf::fix_t{235}, salf::fix_t{234},
        salf::fix_t{233}, salf::fix_t{233}, salf::fix_t{232}, salf::fix_t{231}, salf::fix_t{231}, salf::fix_t{230},
        salf::fix_t{229}, salf::fix_t{229}, salf::fix_t{228}, salf::fix_t{227}, salf::fix_t{227}, salf::fix_t{226},
        salf::fix_t{225}, salf::fix_t{224}, salf::fix_t{224}, salf::fix_t{223}, salf::fix_t{222}, salf::fix_t{221},
        salf::fix_t{220}, salf::fix_t{220}, salf::fix_t{219}, salf::fix_t{218}, salf::fix_t{217}, salf::fix_t{216},
        salf::fix_t{215}, salf::fix_t{215}, salf::fix_t{214}, salf::fix_t{213}, salf::fix_t{212}, salf::fix_t{211},
        salf::fix_t{210}, salf::fix_t{209}, salf::fix_t{208}, salf::fix_t{207}, salf::fix_t{207}, salf::fix_t{206},
        salf::fix_t{205}, salf::fix_t{204}, salf::fix_t{203}, salf::fix_t{202}, salf::fix_t{201}, salf::fix_t{200},
        salf::fix_t{199}, salf::fix_t{198}, salf::fix_t{197}, salf::fix_t{196}, salf::fix_t{195}, salf::fix_t{194},
        salf::fix_t{193}, salf::fix_t{192}, salf::fix_t{191}, salf::fix_t{190}, salf::fix_t{189}, salf::fix_t{188},
        salf::fix_t{186}, salf::fix_t{185}, salf::fix_t{184}, salf::fix_t{183}, salf::fix_t{182}, salf::fix_t{181},
        salf::fix_t{180}, salf::fix_t{179}, salf::fix_t{178}, salf::fix_t{177}, salf::fix_t{175}, salf::fix_t{174},
        salf::fix_t{173}, salf::fix_t{172}, salf::fix_t{171}, salf::fix_t{170}, salf::fix_t{168}, salf::fix_t{167},
        salf::fix_t{166}, salf::fix_t{165}, salf::fix_t{164}, salf::fix_t{162}, salf::fix_t{161}, salf::fix_t{160},
        salf::fix_t{159}, salf::fix_t{157}, salf::fix_t{156}, salf::fix_t{155}, salf::fix_t{154}, salf::fix_t{152},
        salf::fix_t{151}, salf::fix_t{150}, salf::fix_t{149}, salf::fix_t{147}, salf::fix_t{146}, salf::fix_t{145},
        salf::fix_t{144}, salf::fix_t{142}, salf::fix_t{141}, salf::fix_t{140}, salf::fix_t{138}, salf::fix_t{137},
        salf::fix_t{136}, salf::fix_t{134}, salf::fix_t{133}, salf::fix_t{132}, salf::fix_t{130}, salf::fix_t{129},
        salf::fix_t{128}, salf::fix_t{126}, salf::fix_t{125}, salf::fix_t{123}, salf::fix_t{122}, salf::fix_t{121},
        salf::fix_t{119}, salf::fix_t{118}, salf::fix_t{117}, salf::fix_t{115}, salf::fix_t{114}, salf::fix_t{112},
        salf::fix_t{111}, salf::fix_t{109}, salf::fix_t{108}, salf::fix_t{107}, salf::fix_t{105}, salf::fix_t{104},
        salf::fix_t{102}, salf::fix_t{101}, salf::fix_t{99}, salf::fix_t{98}, salf::fix_t{97}, salf::fix_t{95},
        salf::fix_t{94}, salf::fix_t{92}, salf::fix_t{91}, salf::fix_t{89}, salf::fix_t{88}, salf::fix_t{86},
        salf::fix_t{85}, salf::fix_t{83}, salf::fix_t{82}, salf::fix_t{80}, salf::fix_t{79}, salf::fix_t{77},
        salf::fix_t{76}, salf::fix_t{74}, salf::fix_t{73}, salf::fix_t{71}, salf::fix_t{70}, salf::fix_t{68},
        salf::fix_t{67}, salf::fix_t{65}, salf::fix_t{64}, salf::fix_t{62}, salf::fix_t{61}, salf::fix_t{59},
        salf::fix_t{58}, salf::fix_t{56}, salf::fix_t{55}, salf::fix_t{53}, salf::fix_t{51}, salf::fix_t{50},
        salf::fix_t{48}, salf::fix_t{47}, salf::fix_t{45}, salf::fix_t{44}, salf::fix_t{42}, salf::fix_t{41},
        salf::fix_t{39}, salf::fix_t{38}, salf::fix_t{36}, salf::fix_t{34}, salf::fix_t{33}, salf::fix_t{31},
        salf::fix_t{30}, salf::fix_t{28}, salf::fix_t{27}, salf::fix_t{25}, salf::fix_t{24}, salf::fix_t{22},
        salf::fix_t{20}, salf::fix_t{19}, salf::fix_t{17}, salf::fix_t{16}, salf::fix_t{14}, salf::fix_t{13},
        salf::fix_t{11}, salf::fix_t{9}, salf::fix_t{8}, salf::fix_t{6}, salf::fix_t{5}, salf::fix_t{3}, salf::fix_t{2},
        salf::fix_t{0}, salf::fix_t{-2}, salf::fix_t{-3}, salf::fix_t{-5}, salf::fix_t{-6}, salf::fix_t{-8},
        salf::fix_t{-9}, salf::fix_t{-11}, salf::fix_t{-13}, salf::fix_t{-14}, salf::fix_t{-16}, salf::fix_t{-17},
        salf::fix_t{-19}, salf::fix_t{-20}, salf::fix_t{-22}, salf::fix_t{-24}, salf::fix_t{-25}, salf::fix_t{-27},
        salf::fix_t{-28}, salf::fix_t{-30}, salf::fix_t{-31}, salf::fix_t{-33}, salf::fix_t{-34}, salf::fix_t{-36},
        salf::fix_t{-38}, salf::fix_t{-39}, salf::fix_t{-41}, salf::fix_t{-42}, salf::fix_t{-44}, salf::fix_t{-45},
        salf::fix_t{-47}, salf::fix_t{-48}, salf::fix_t{-50}, salf::fix_t{-51}, salf::fix_t{-53}, salf::fix_t{-55},
        salf::fix_t{-56}, salf::fix_t{-58}, salf::fix_t{-59}, salf::fix_t{-61}, salf::fix_t{-62}, salf::fix_t{-64},
        salf::fix_t{-65}, salf::fix_t{-67}, salf::fix_t{-68}, salf::fix_t{-70}, salf::fix_t{-71}, salf::fix_t{-73},
        salf::fix_t{-74}, salf::fix_t{-76}, salf::fix_t{-77}, salf::fix_t{-79}, salf::fix_t{-80}, salf::fix_t{-82},
        salf::fix_t{-83}, salf::fix_t{-85}, salf::fix_t{-86}, salf::fix_t{-88}, salf::fix_t{-89}, salf::fix_t{-91},
        salf::fix_t{-92}, salf::fix_t{-94}, salf::fix_t{-95}, salf::fix_t{-97}, salf::fix_t{-98}, salf::fix_t{-99},
        salf::fix_t{-101}, salf::fix_t{-102}, salf::fix_t{-104}, salf::fix_t{-105}, salf::fix_t{-107},
        salf::fix_t{-108}, salf::fix_t{-109}, salf::fix_t{-111}, salf::fix_t{-112}, salf::fix_t{-114},
        salf::fix_t{-115}, salf::fix_t{-117}, salf::fix_t{-118}, salf::fix_t{-119}, salf::fix_t{-121},
        salf::fix_t{-122}, salf::fix_t{-123}, salf::fix_t{-125}, salf::fix_t{-126}, salf::fix_t{-128},
        salf::fix_t{-129}, salf::fix_t{-130}, salf::fix_t{-132}, salf::fix_t{-133}, salf::fix_t{-134},
        salf::fix_t{-136}, salf::fix_t{-137}, salf::fix_t{-138}, salf::fix_t{-140}, salf::fix_t{-141},
        salf::fix_t{-142}, salf::fix_t{-144}, salf::fix_t{-145}, salf::fix_t{-146}, salf::fix_t{-147},
        salf::fix_t{-149}, salf::fix_t{-150}, salf::fix_t{-151}, salf::fix_t{-152}, salf::fix_t{-154},
        salf::fix_t{-155}, salf::fix_t{-156}, salf::fix_t{-157}, salf::fix_t{-159}, salf::fix_t{-160},
        salf::fix_t{-161}, salf::fix_t{-162}, salf::fix_t{-164}, salf::fix_t{-165}, salf::fix_t{-166},
        salf::fix_t{-167}, salf::fix_t{-168}, salf::fix_t{-170}, salf::fix_t{-171}, salf::fix_t{-172},
        salf::fix_t{-173}, salf::fix_t{-174}, salf::fix_t{-175}, salf::fix_t{-177}, salf::fix_t{-178},
        salf::fix_t{-179}, salf::fix_t{-180}, salf::fix_t{-181}, salf::fix_t{-182}, salf::fix_t{-183},
        salf::fix_t{-184}, salf::fix_t{-185}, salf::fix_t{-186}, salf::fix_t{-188}, salf::fix_t{-189},
        salf::fix_t{-190}, salf::fix_t{-191}, salf::fix_t{-192}, salf::fix_t{-193}, salf::fix_t{-194},
        salf::fix_t{-195}, salf::fix_t{-196}, salf::fix_t{-197}, salf::fix_t{-198}, salf::fix_t{-199},
        salf::fix_t{-200}, salf::fix_t{-201}, salf::fix_t{-202}, salf::fix_t{-203}, salf::fix_t{-204},
        salf::fix_t{-205}, salf::fix_t{-206}, salf::fix_t{-207}, salf::fix_t{-207}, salf::fix_t{-208},
        salf::fix_t{-209}, salf::fix_t{-210}, salf::fix_t{-211}, salf::fix_t{-212}, salf::fix_t{-213},
        salf::fix_t{-214}, salf::fix_t{-215}, salf::fix_t{-215}, salf::fix_t{-216}, salf::fix_t{-217},
        salf::fix_t{-218}, salf::fix_t{-219}, salf::fix_t{-220}, salf::fix_t{-220}, salf::fix_t{-221},
        salf::fix_t{-222}, salf::fix_t{-223}, salf::fix_t{-224}, salf::fix_t{-224}, salf::fix_t{-225},
        salf::fix_t{-226}, salf::fix_t{-227}, salf::fix_t{-227}, salf::fix_t{-228}, salf::fix_t{-229},
        salf::fix_t{-229}, salf::fix_t{-230}, salf::fix_t{-231}, salf::fix_t{-231}, salf::fix_t{-232},
        salf::fix_t{-233}, salf::fix_t{-233}, salf::fix_t{-234}, salf::fix_t{-235}, salf::fix_t{-235},
        salf::fix_t{-236}, salf::fix_t{-237}, salf::fix_t{-237}, salf::fix_t{-238}, salf::fix_t{-238},
        salf::fix_t{-239}, salf::fix_t{-239}, salf::fix_t{-240}, salf::fix_t{-241}, salf::fix_t{-241},
        salf::fix_t{-242}, salf::fix_t{-242}, salf::fix_t{-243}, salf::fix_t{-243}, salf::fix_t{-244},
        salf::fix_t{-244}, salf::fix_t{-245}, salf::fix_t{-245}, salf::fix_t{-245}, salf::fix_t{-246},
        salf::fix_t{-246}, salf::fix_t{-247}, salf::fix_t{-247}, salf::fix_t{-248}, salf::fix_t{-248},
        salf::fix_t{-248}, salf::fix_t{-249}, salf::fix_t{-249}, salf::fix_t{-249}, salf::fix_t{-250},
        salf::fix_t{-250}, salf::fix_t{-250}, salf::fix_t{-251}, salf::fix_t{-251}, salf::fix_t{-251},
        salf::fix_t{-252}, salf::fix_t{-252}, salf::fix_t{-252}, salf::fix_t{-252}, salf::fix_t{-253},
        salf::fix_t{-253}, salf::fix_t{-253}, salf::fix_t{-253}, salf::fix_t{-254}, salf::fix_t{-254},
        salf::fix_t{-254}, salf::fix_t{-254}, salf::fix_t{-254}, salf::fix_t{-255}, salf::fix_t{-255},
        salf::fix_t{-255}, salf::fix_t{-255}, salf::fix_t{-255}, salf::fix_t{-255}, salf::fix_t{-255},
        salf::fix_t{-256}, salf::fix_t{-256}, salf::fix_t{-256}, salf::fix_t{-256}, salf::fix_t{-256},
        salf::fix_t{-256}, salf::fix_t{-256}, salf::fix_t{-256}, salf::fix_t{-256}, salf::fix_t{-256},
        salf::fix_t{-256}, salf::fix_t{-256}, salf::fix_t{-256}, salf::fix_t{-256}, salf::fix_t{-256},
        salf::fix_t{-256}, salf::fix_t{-256}, salf::fix_t{-256}, salf::fix_t{-256}, salf::fix_t{-256},
        salf::fix_t{-256}, salf::fix_t{-255}, salf::fix_t{-255}, salf::fix_t{-255}, salf::fix_t{-255},
        salf::fix_t{-255}, salf::fix_t{-255}, salf::fix_t{-255}, salf::fix_t{-254}, salf::fix_t{-254},
        salf::fix_t{-254}, salf::fix_t{-254}, salf::fix_t{-254}, salf::fix_t{-253}, salf::fix_t{-253},
        salf::fix_t{-253}, salf::fix_t{-253}, salf::fix_t{-252}, salf::fix_t{-252}, salf::fix_t{-252},
        salf::fix_t{-252}, salf::fix_t{-251}, salf::fix_t{-251}, salf::fix_t{-251}, salf::fix_t{-250},
        salf::fix_t{-250}, salf::fix_t{-250}, salf::fix_t{-249}, salf::fix_t{-249}, salf::fix_t{-249},
        salf::fix_t{-248}, salf::fix_t{-248}, salf::fix_t{-248}, salf::fix_t{-247}, salf::fix_t{-247},
        salf::fix_t{-246}, salf::fix_t{-246}, salf::fix_t{-245}, salf::fix_t{-245}, salf::fix_t{-245},
        salf::fix_t{-244}, salf::fix_t{-244}, salf::fix_t{-243}, salf::fix_t{-243}, salf::fix_t{-242},
        salf::fix_t{-242}, salf::fix_t{-241}, salf::fix_t{-241}, salf::fix_t{-240}, salf::fix_t{-239},
        salf::fix_t{-239}, salf::fix_t{-238}, salf::fix_t{-238}, salf::fix_t{-237}, salf::fix_t{-237},
        salf::fix_t{-236}, salf::fix_t{-235}, salf::fix_t{-235}, salf::fix_t{-234}, salf::fix_t{-233},
        salf::fix_t{-233}, salf::fix_t{-232}, salf::fix_t{-231}, salf::fix_t{-231}, salf::fix_t{-230},
        salf::fix_t{-229}, salf::fix_t{-229}, salf::fix_t{-228}, salf::fix_t{-227}, salf::fix_t{-227},
        salf::fix_t{-226}, salf::fix_t{-225}, salf::fix_t{-224}, salf::fix_t{-224}, salf::fix_t{-223},
        salf::fix_t{-222}, salf::fix_t{-221}, salf::fix_t{-220}, salf::fix_t{-220}, salf::fix_t{-219},
        salf::fix_t{-218}, salf::fix_t{-217}, salf::fix_t{-216}, salf::fix_t{-215}, salf::fix_t{-215},
        salf::fix_t{-214}, salf::fix_t{-213}, salf::fix_t{-212}, salf::fix_t{-211}, salf::fix_t{-210},
        salf::fix_t{-209}, salf::fix_t{-208}, salf::fix_t{-207}, salf::fix_t{-207}, salf::fix_t{-206},
        salf::fix_t{-205}, salf::fix_t{-204}, salf::fix_t{-203}, salf::fix_t{-202}, salf::fix_t{-201},
        salf::fix_t{-200}, salf::fix_t{-199}, salf::fix_t{-198}, salf::fix_t{-197}, salf::fix_t{-196},
        salf::fix_t{-195}, salf::fix_t{-194}, salf::fix_t{-193}, salf::fix_t{-192}, salf::fix_t{-191},
        salf::fix_t{-190}, salf::fix_t{-189}, salf::fix_t{-188}, salf::fix_t{-186}, salf::fix_t{-185},
        salf::fix_t{-184}, salf::fix_t{-183}, salf::fix_t{-182}, salf::fix_t{-181}, salf::fix_t{-180},
        salf::fix_t{-179}, salf::fix_t{-178}, salf::fix_t{-177}, salf::fix_t{-175}, salf::fix_t{-174},
        salf::fix_t{-173}, salf::fix_t{-172}, salf::fix_t{-171}, salf::fix_t{-170}, salf::fix_t{-168},
        salf::fix_t{-167}, salf::fix_t{-166}, salf::fix_t{-165}, salf::fix_t{-164}, salf::fix_t{-162},
        salf::fix_t{-161}, salf::fix_t{-160}, salf::fix_t{-159}, salf::fix_t{-157}, salf::fix_t{-156},
        salf::fix_t{-155}, salf::fix_t{-154}, salf::fix_t{-152}, salf::fix_t{-151}, salf::fix_t{-150},
        salf::fix_t{-149}, salf::fix_t{-147}, salf::fix_t{-146}, salf::fix_t{-145}, salf::fix_t{-144},
        salf::fix_t{-142}, salf::fix_t{-141}, salf::fix_t{-140}, salf::fix_t{-138}, salf::fix_t{-137},
        salf::fix_t{-136}, salf::fix_t{-134}, salf::fix_t{-133}, salf::fix_t{-132}, salf::fix_t{-130},
        salf::fix_t{-129}, salf::fix_t{-128}, salf::fix_t{-126}, salf::fix_t{-125}, salf::fix_t{-123},
        salf::fix_t{-122}, salf::fix_t{-121}, salf::fix_t{-119}, salf::fix_t{-118}, salf::fix_t{-117},
        salf::fix_t{-115}, salf::fix_t{-114}, salf::fix_t{-112}, salf::fix_t{-111}, salf::fix_t{-109},
        salf::fix_t{-108}, salf::fix_t{-107}, salf::fix_t{-105}, salf::fix_t{-104}, salf::fix_t{-102},
        salf::fix_t{-101}, salf::fix_t{-99}, salf::fix_t{-98}, salf::fix_t{-97}, salf::fix_t{-95}, salf::fix_t{-94},
        salf::fix_t{-92}, salf::fix_t{-91}, salf::fix_t{-89}, salf::fix_t{-88}, salf::fix_t{-86}, salf::fix_t{-85},
        salf::fix_t{-83}, salf::fix_t{-82}, salf::fix_t{-80}, salf::fix_t{-79}, salf::fix_t{-77}, salf::fix_t{-76},
        salf::fix_t{-74}, salf::fix_t{-73}, salf::fix_t{-71}, salf::fix_t{-70}, salf::fix_t{-68}, salf::fix_t{-67},
        salf::fix_t{-65}, salf::fix_t{-64}, salf::fix_t{-62}, salf::fix_t{-61}, salf::fix_t{-59}, salf::fix_t{-58},
        salf::fix_t{-56}, salf::fix_t{-55}, salf::fix_t{-53}, salf::fix_t{-51}, salf::fix_t{-50}, salf::fix_t{-48},
        salf::fix_t{-47}, salf::fix_t{-45}, salf::fix_t{-44}, salf::fix_t{-42}, salf::fix_t{-41}, salf::fix_t{-39},
        salf::fix_t{-38}, salf::fix_t{-36}, salf::fix_t{-34}, salf::fix_t{-33}, salf::fix_t{-31}, salf::fix_t{-30},
        salf::fix_t{-28}, salf::fix_t{-27}, salf::fix_t{-25}, salf::fix_t{-24}, salf::fix_t{-22}, salf::fix_t{-20},
        salf::fix_t{-19}, salf::fix_t{-17}, salf::fix_t{-16}, salf::fix_t{-14}, salf::fix_t{-13}, salf::fix_t{-11},
        salf::fix_t{-9}, salf::fix_t{-8}, salf::fix_t{-6}, salf::fix_t{-5}, salf::fix_t{-3}, salf::fix_t{-2}
    };


    constexpr std::array<uint16_t, ATAN_TABLE_SIZE> ATAN_CACHE = {
        0, 20, 41, 61, 81, 102, 122, 143, 163, 183, 204, 224, 244, 265, 285, 305, 326, 346, 367, 387, 407, 428, 448,
        468, 489, 509, 529, 550, 570, 590, 610, 631, 651, 671, 692, 712, 732, 752, 773, 793, 813, 833, 854, 874, 894,
        914, 935, 955, 975, 995, 1015, 1036, 1056, 1076, 1096, 1116, 1136, 1156, 1177, 1197, 1217, 1237, 1257, 1277,
        1297, 1317, 1337, 1357, 1377, 1397, 1417, 1437, 1457, 1477, 1497, 1517, 1537, 1557, 1577, 1597, 1617, 1637,
        1656, 1676, 1696, 1716, 1736, 1756, 1775, 1795, 1815, 1835, 1854, 1874, 1894, 1914, 1933, 1953, 1973, 1992,
        2012, 2031, 2051, 2071, 2090, 2110, 2129, 2149, 2168, 2188, 2207, 2227, 2246, 2266, 2285, 2305, 2324, 2343,
        2363, 2382, 2401, 2421, 2440, 2459, 2478, 2498, 2517, 2536, 2555, 2574, 2594, 2613, 2632, 2651, 2670, 2689,
        2708, 2727, 2746, 2765, 2784, 2803, 2822, 2841, 2860, 2879, 2897, 2916, 2935, 2954, 2973, 2991, 3010, 3029,
        3047, 3066, 3085, 3103, 3122, 3141, 3159, 3178, 3196, 3215, 3233, 3252, 3270, 3289, 3307, 3325, 3344, 3362,
        3380, 3399, 3417, 3435, 3453, 3472, 3490, 3508, 3526, 3544, 3562, 3580, 3599, 3617, 3635, 3653, 3670, 3688,
        3706, 3724, 3742, 3760, 3778, 3796, 3813, 3831, 3849, 3867, 3884, 3902, 3920, 3937, 3955, 3972, 3990, 4007,
        4025, 4042, 4060, 4077, 4095, 4112, 4129, 4147, 4164, 4181, 4199, 4216, 4233, 4250, 4267, 4284, 4302, 4319,
        4336, 4353, 4370, 4387, 4404, 4421, 4438, 4454, 4471, 4488, 4505, 4522, 4539, 4555, 4572, 4589, 4605, 4622,
        4639, 4655, 4672, 4688, 4705, 4721, 4738, 4754, 4771, 4787, 4803, 4820, 4836, 4852, 4869, 4885, 4901, 4917,
        4933, 4949, 4966, 4982, 4998, 5014, 5030, 5046, 5062, 5078, 5094, 5109, 5125, 5141, 5157, 5173, 5188, 5204,
        5220, 5235, 5251, 5267, 5282, 5298, 5313, 5329, 5344, 5360, 5375, 5391, 5406, 5421, 5437, 5452, 5467, 5483,
        5498, 5513, 5528, 5543, 5559, 5574, 5589, 5604, 5619, 5634, 5649, 5664, 5679, 5694, 5708, 5723, 5738, 5753,
        5768, 5782, 5797, 5812, 5826, 5841, 5856, 5870, 5885, 5899, 5914, 5928, 5943, 5957, 5972, 5986, 6000, 6015,
        6029, 6043, 6058, 6072, 6086, 6100, 6114, 6128, 6142, 6157, 6171, 6185, 6199, 6213, 6227, 6240, 6254, 6268,
        6282, 6296, 6310, 6323, 6337, 6351, 6365, 6378, 6392, 6406, 6419, 6433, 6446, 6460, 6473, 6487, 6500, 6514,
        6527, 6540, 6554, 6567, 6580, 6594, 6607, 6620, 6633, 6646, 6660, 6673, 6686, 6699, 6712, 6725, 6738, 6751,
        6764, 6777, 6790, 6803, 6815, 6828, 6841, 6854, 6867, 6879, 6892, 6905, 6917, 6930, 6943, 6955, 6968, 6980,
        6993, 7005, 7018, 7030, 7043, 7055, 7068, 7080, 7092, 7105, 7117, 7129, 7141, 7154, 7166, 7178, 7190, 7202,
        7214, 7226, 7238, 7250, 7262, 7274, 7286, 7298, 7310, 7322, 7334, 7346, 7358, 7369, 7381, 7393, 7405, 7416,
        7428, 7440, 7451, 7463, 7475, 7486, 7498, 7509, 7521, 7532, 7544, 7555, 7566, 7578, 7589, 7601, 7612, 7623,
        7635, 7646, 7657, 7668, 7679, 7691, 7702, 7713, 7724, 7735, 7746, 7757, 7768, 7779, 7790, 7801, 7812, 7823,
        7834, 7845, 7856, 7866, 7877, 7888, 7899, 7910, 7920, 7931, 7942, 7952, 7963, 7974, 7984, 7995, 8005, 8016,
        8026, 8037, 8047, 8058, 8068, 8079, 8089, 8100, 8110, 8120, 8131, 8141, 8151, 8161, 8172, 8182, 8192
    };


    [[nodiscard]] constexpr uint64_t integerSqrt64(uint64_t value) noexcept {
        uint64_t root = 0;
        uint64_t bit = uint64_t{1} << 62;

        while (bit > value) {
            bit >>= 2;
        }
        while (bit != 0) {
            if (value >= root + bit) {
                value -= root + bit;
                root = (root >> 1) + bit;
            } else {
                root >>= 1;
            }
            bit >>= 2;
        }
        return root;
    }
}

namespace salf::math {
    [[nodiscard]] bool isInRange(fix_t dx, fix_t dy, fix_t rSum) noexcept {
        if (rSum.num < 0) return false;

        int64_t wideDeltaX = dx.num;
        int64_t wideDeltaY = dy.num;
        uint64_t wideRadiusSum = static_cast<uint64_t>(rSum.num);

        uint64_t distanceSquared = static_cast<uint64_t>(wideDeltaX * wideDeltaX) +
                                   static_cast<uint64_t>(wideDeltaY * wideDeltaY);
        uint64_t radiusSquared = wideRadiusSum * wideRadiusSum;

        return distanceSquared <= radiusSquared;
    }

    [[nodiscard]] fix_t sqrt(fix_t value) noexcept {
        if (value.num <= 0) return {0};
        uint64_t shifted = static_cast<uint64_t>(value.num) << (fix_t::SHIFT + 2);
        return {static_cast<int32_t>((integerSqrt64(shifted) + 1) >> 1)};
    }

    [[nodiscard]] fix_t distance(fix_t x1, fix_t y1, fix_t x2, fix_t y2) noexcept {
        int64_t deltaX = static_cast<int64_t>(x2.num) - x1.num;
        int64_t deltaY = static_cast<int64_t>(y2.num) - y1.num;

        uint64_t distanceSquared = static_cast<uint64_t>(deltaX * deltaX) +
                                   static_cast<uint64_t>(deltaY * deltaY);
        if (distanceSquared == 0) return {0};

        if (distanceSquared < (uint64_t{1} << 62)) {
            return {static_cast<int32_t>((integerSqrt64(distanceSquared << 2) + 1) >> 1)};
        }
        return {static_cast<int32_t>(integerSqrt64(distanceSquared))};
    }

    [[nodiscard]] fix_t distance(fix_t deltaX, fix_t deltaY) noexcept {
        return distance({0}, {0}, deltaX, deltaY);
    }

    [[nodiscard]] fix_t sin(angle_t angle) noexcept {
        return SIN_CACHE[angle.num >> SIN_INDEX_SHIFT];
    }

    [[nodiscard]] fix_t cos(angle_t angle) noexcept {
        uint16_t shiftedAngle = static_cast<uint16_t>(angle.num + angle_t::QUARTER);
        return SIN_CACHE[shiftedAngle >> SIN_INDEX_SHIFT];
    }

    [[nodiscard]] angle_t atan2(fix_t y, fix_t x) noexcept {
        if (x.num == 0 && y.num == 0) return angle_t{0};

        int64_t absX = x.num < 0 ? -static_cast<int64_t>(x.num) : x.num;
        int64_t absY = y.num < 0 ? -static_cast<int64_t>(y.num) : y.num;

        bool flip = absY > absX;
        int64_t numerator = flip ? absX : absY;
        int64_t denominator = flip ? absY : absX;

        int32_t lutIndex = static_cast<int32_t>(
            ((numerator << ATAN_TABLE_BITS) + (denominator >> 1)) / denominator
        );
        uint16_t angle = ATAN_CACHE[lutIndex];

        if (flip) {
            angle = static_cast<uint16_t>(angle_t::QUARTER - angle);
        }

        if (x.num < 0) {
            if (y.num >= 0) {
                angle = static_cast<uint16_t>(angle_t::HALF - angle);
            } else {
                angle = static_cast<uint16_t>(angle_t::HALF + angle);
            }
        } else if (y.num < 0) {
            angle = static_cast<uint16_t>(-angle);
        }

        return angle_t{angle};
    }
}
