export default async (context) => {
  return {
    "tool.execute.before": async (input, output) => {
      console.log("[watch] BEFORE tool:", input.tool)
      console.log("[watch] RAW input:", JSON.stringify(input))
    },
  }
}