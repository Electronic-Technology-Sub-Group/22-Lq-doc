---
版本: 1.20.1
类型: Forge
---
Minecraft 世界生成主要分两部分 - `PlacedFeature` 和 `ConfiguredFeature`，二者都作为数据包存储
# ConfiguredFeature

`ConfiguredFeature` 用于具体生成的过程，由 `Feature` 和 `FeatureConfiguration` 组成，前者为生成器类型，后者为生成器配置。

Minecraft 内置的 `Feature` 可以在 `net.minecraft.world.level.levelgen.feature.Feature` 中找到，不同 `Feature` 对应不同 `FeatureConfiguration` 类型
# PlacedFeature

`PlacedFeature` 用于确定在哪里生成
# 数据包生成
# 自定义 Feature
# 自定义 PlacementModifier
