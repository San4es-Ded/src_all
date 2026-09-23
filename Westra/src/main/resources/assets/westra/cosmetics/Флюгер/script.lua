-- ==========================================================
--        ПОЛНЫЕ НАСТРОЙКИ ПРИСЕДА (ПОД НОВУЮ СТРУКТУРУ)
-- ==========================================================
-- POS = {X, Y, Z} — Смещение детали (в пикселях)
-- ROT = {X, Y, Z} — Поворот детали (в градусах)

-- ТЕЛО (Body)
local CROUCH_BODY_POS = {0, 1, 0}
local CROUCH_BODY_ROT = {10, 0, 0}

-- ГОЛОВА (Head) — лежит внутри Body, наклоняется вместе с ним
local CROUCH_HEAD_POS = {0, 3.8, 0}
local CROUCH_HEAD_ROT = {10, 0, 0}

-- ПРАВАЯ РУКА (RightArm) — теперь лежит в root, настраиваем отдельно
local CROUCH_R_ARM_POS = {0, 1, -1.5}      -- Смещение правой руки при приседе
local CROUCH_R_ARM_ROT = {-10, 0, -10}

-- ЛЕВАЯ РУКА (LeftArm) — теперь лежит в root, настраиваем отдельно
local CROUCH_L_ARM_POS = {0, 1, -1.5}       -- Смещение левой руки при приседе
local CROUCH_L_ARM_ROT = {-10, 0, 10}

-- ПРАВАЯ НОГА (RightLeg)
local CROUCH_R_LEG_POS = {0, 0, -6}
local CROUCH_R_LEG_ROT = {-25, 0, 0}

-- ЛЕВАЯ НОГА (LeftLeg)
local CROUCH_L_LEG_POS = {0, 0, -6}
local CROUCH_L_LEG_ROT = {-25, 0, 0}
-- ==========================================================


-- Отключаем ванильного Стива и его плащ
vanilla_model.PLAYER:setVisible(false)
vanilla_model.CAPE:setVisible(false)

-- Хак невидимости для себя (если в конфиге/моде разрешено)
if renderer and renderer.setRenderSelfInvisible then
    renderer:setRenderSelfInvisible(true)
end

local root = models.avatar.root

-- Автоматическая склейка брони и костей
if root then
    if root.Body then root.Body:setParentType("BODY") end
    if root.Body and root.Body.Head then root.Body.Head:setParentType("HEAD") end
    
    if root.RightArm then root.RightArm:setParentType("RIGHT_ARM") end
    if root.LeftArm then root.LeftArm:setParentType("LEFT_ARM") end
    
    if root.RightLeg then root.RightLeg:setParentType("RIGHT_LEG") end
    if root.LeftLeg then root.LeftLeg:setParentType("LEFT_LEG") end
end

-- 1. ЛОГИКА ОТОБРАЖЕНИЯ И ПЕРВОГО ЛИЦА (БЕЗ ПРОВЕРОК НА ХОСТА ДЛЯ REPLAY MOD)
function events.render(delta, mode)
    if not root then return end
    
    -- Полностью убираем любые скрытия модели, чтобы она рендерилась для твоего клона
    root:setVisible(true)
    
    -- Логика первого лица
    if mode == "FIRST_PERSON" then
        if root.Body then root.Body:setVisible(false) end
        if root.LeftArm then root.LeftArm:setVisible(false) end
        if root.RightArm then root.RightArm:setVisible(true) end
        if root.RightLeg then root.RightLeg:setVisible(false) end
        if root.LeftLeg then root.LeftLeg:setVisible(false) end
    else
        -- В обычном режиме показываем всё
        if root.Body then root.Body:setVisible(true) end
        if root.LeftArm then root.LeftArm:setVisible(true) end
        if root.RightArm then root.RightArm:setVisible(true) end
        if root.RightLeg then root.RightLeg:setVisible(true) end
        if root.LeftLeg then root.LeftLeg:setVisible(true) end
    end
end

-- 2. УПРАВЛЕНИЕ АНИМАЦИЯМИ И ДВИЖЕНИЕМ
function events.tick()
    if not root then return end
    
    -- Если играет эмоция Emotecraft — скрипт засыпает и оставляет весёлые баги с головой на месте
    if emotecraft and emotecraft:isPlayingEmote() then
        return
    end

    local velocity = player:getVelocity()
    local speed = (velocity.x^2 + velocity.z^2)^0.5
    local is_crouching = player:isCrouching()
    
    local body = root.Body
    local head = body and body.Head
    local r_arm = root.RightArm
    local l_arm = root.LeftArm or root.left_arm
    local r_leg = root.RightLeg
    local l_leg = root.LeftLeg

    if is_crouching then
        -- Настройки ТЕЛА
        if body then
            body:setPos(CROUCH_BODY_POS[1], CROUCH_BODY_POS[2], CROUCH_BODY_POS[3])
            body:setRot(CROUCH_BODY_ROT[1], CROUCH_BODY_ROT[2], CROUCH_BODY_ROT[3])
        end
        
        -- Настройки ГОЛОВЫ
        if head then
            head:setPos(CROUCH_HEAD_POS[1], CROUCH_HEAD_POS[2], CROUCH_HEAD_POS[3])
            head:setRot(CROUCH_HEAD_ROT[1], CROUCH_HEAD_ROT[2], CROUCH_HEAD_ROT[3])
        end
        
        -- Настройки РУК
        if r_arm then 
            r_arm:setPos(CROUCH_R_ARM_POS[1], CROUCH_R_ARM_POS[2], CROUCH_R_ARM_POS[3])
            r_arm:setRot(CROUCH_R_ARM_ROT[1], CROUCH_R_ARM_ROT[2], CROUCH_R_ARM_ROT[3]) 
        end
        if l_arm then 
            l_arm:setPos(CROUCH_L_ARM_POS[1], CROUCH_L_ARM_POS[2], CROUCH_L_ARM_POS[3])
            l_arm:setRot(CROUCH_L_ARM_ROT[1], CROUCH_L_ARM_ROT[2], CROUCH_L_ARM_ROT[3]) 
        end
        
        -- Настройки НОГ
        if r_leg then 
            r_leg:setPos(CROUCH_R_LEG_POS[1], CROUCH_R_LEG_POS[2], CROUCH_R_LEG_POS[3])
            r_leg:setRot(CROUCH_R_LEG_ROT[1], CROUCH_R_LEG_ROT[2], CROUCH_R_LEG_ROT[3]) 
        end
        if l_leg then 
            l_leg:setPos(CROUCH_L_LEG_POS[1], CROUCH_L_LEG_POS[2], CROUCH_L_LEG_POS[3])
            l_leg:setRot(CROUCH_L_LEG_ROT[1], CROUCH_L_LEG_ROT[2], CROUCH_L_LEG_ROT[3]) 
        end
    else
        -- СБРОС В ДЕФОЛТ
        if body then
            body:setPos(0, 0, 0)
            body:setRot(0, 0, 0)
        end
        if head then
            head:setPos(0, 0, 0)
            head:setRot(0, 0, 0)
        end
        if r_arm then r_arm:setPos(0, 0, 0) end
        if l_arm then l_arm:setPos(0, 0, 0) end
        if r_leg then r_leg:setPos(0, 0, 0) end
        if l_leg then l_leg:setPos(0, 0, 0) end
        
        -- Анимация ходьбы
        if speed > 0.01 then
            local time = world.getTime() * 0.4
            local angle = math.sin(time) * 30

            if r_leg then r_leg:setRot(-angle, 0, 0) end
            if l_leg then l_leg:setRot(angle, 0, 0) end
            if r_arm then r_arm:setRot(0, 0, -20) end
            if l_arm then l_arm:setRot(0, 0, 20) end
        else
            -- Стойка смирно
            if r_leg then r_leg:setRot(0, 0, 0) end
            if l_leg then l_leg:setRot(0, 0, 0) end
            if r_arm then r_arm:setRot(0, 0, -20) end
            if l_arm then l_arm:setRot(0, 0, 20) end
        end
    end
end