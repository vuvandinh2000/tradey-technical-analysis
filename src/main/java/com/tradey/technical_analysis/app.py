import asyncio

from src.container import Container


async def main():
    dependencies = await Container.create()
    technical_analysis_use_case = dependencies.use_case.technical_analysis
    await technical_analysis_use_case.execute()


def lambda_handler(event, context):
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    try:
        loop.run_until_complete(main())
    finally:
        loop.close()


if __name__ == "__main__":
    lambda_handler(None, None)
