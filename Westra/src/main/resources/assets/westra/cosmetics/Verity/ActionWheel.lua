local model = models.verity

local TEX = {
    Verity = textures["Verity"],
    VerityTalk = textures["VerityTalk"],
    VerityDisturbing = textures["VerityDisturbing"],
    VerityDisturbingTalk = textures["VerityDisturbingTalk"],
    VerityDisturbingYell = textures["VerityDisturbingYell"],
    VerityBad = textures["VerityBad"],
    VerityVeryBad = textures["VerityVeryBad"]
}

local function setState(tex, name)
    if not tex then
        print("[ERROR] Missing texture:", name)
        return
    end

    model:setPrimaryTexture("CUSTOM", tex)
end

local wheel = action_wheel:newPage()

local function add(name)
    wheel:newAction()
        :title(name)
        :item("minecraft:paper")
        :onLeftClick(function()
            setState(TEX[name], name)
        end)
end

add("Verity")
add("VerityTalk")
add("VerityDisturbing")
add("VerityDisturbingTalk")
add("VerityDisturbingYell")
add("VerityBad")
add("VerityVeryBad")

action_wheel:setPage(wheel)

setState(TEX.Verity, "Verity")